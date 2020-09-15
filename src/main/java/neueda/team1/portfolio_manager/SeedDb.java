package neueda.team1.portfolio_manager;

import neueda.team1.portfolio_manager.entity.*;
import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import neueda.team1.portfolio_manager.repository.*;
import neueda.team1.portfolio_manager.repository.repository_ytx.PortfolioRepository;
import neueda.team1.portfolio_manager.util.NumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SeedDb {
    @Value("${initdb}")
    private boolean initDb;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final List<String> INIT_SYMBOLS = Arrays.asList("EBAY", "ZNGA", "YELP", "YNDX", "SPWR",
            "SSYS", "SPLK", "SAP", "RP", "PANW", "LN", "IRBT", "ILMN", "IBM", "GRPN", "GDOT", "GOGO",
            "FB", "FEYE", "ENV", "BYND", "Y", "ADBE", "T", "MMM");
    public static final int INIT_BALANCE = 10000000;
    @Value("${hummingbird.apikey.6}")
    private String API_KEY;

    private final PortfolioRepository portfolioRepository;
    private final SecurityRepository securityRepository;
    private final SecurityHistoryRepository securityHistoryRepository;
    private final UserRepository userRepository;
    private final DailyPositionRepository dailyPositionRepository;
    private final TransactionRepository transactionRepository;
    private final TeamPortfolioRepository teamPortfolioRepository;

    public SeedDb(PortfolioRepository portfolioRepository, SecurityRepository securityRepository,
                  SecurityHistoryRepository securityHistoryRepository, UserRepository userRepository,
                  DailyPositionRepository dailyPositionRepository, TransactionRepository transactionRepository,
                  TeamPortfolioRepository teamPortfolioRepository) {
        this.portfolioRepository = portfolioRepository;
        this.securityRepository = securityRepository;
        this.securityHistoryRepository = securityHistoryRepository;
        this.userRepository = userRepository;
        this.dailyPositionRepository = dailyPositionRepository;
        this.transactionRepository = transactionRepository;
        this.teamPortfolioRepository = teamPortfolioRepository;
    }

    @PostConstruct
    public void initDb() {
        LOGGER.warn("Initializing DB");
        if (initDb) {
            this.initUser();
            this.initTeamPortfolio();
//            this.initPortfolio();//deprecated
            this.initSecurities(); // Comment out this line if your database is already initiated
//            this.initPortfolioNames();//deprecated
            this.initDailyPositions();
            this.initTransaction();
            this.initBankAccount();
        }
    }

    private void initUser() {
        LOGGER.info("Adding user documents to user collection...");
        userRepository.deleteAll();
        userRepository.save(new User("user0001", "caoyu", "pwd", "caoyu@neueda.com"));
//        userRepository.save(new User("user0002", "yutongxin", "pwd", "yutongxin@neueda.com"));
//        userRepository.save(new User("user0003", "duwenyuan", "pwd", "duwenyuan@neueda.com"));
//        userRepository.save(new User("user0004", "hezhi", "pwd", "hezhi@neueda.com"));
        LOGGER.info("Adding user documents to user collection: Done");
    }

    private void initTeamPortfolio() {
        LOGGER.info("Adding teamPortfolio collection...");
        teamPortfolioRepository.deleteAll();
        teamPortfolioRepository.save(new TeamPortfolio("portfolio01", "caoyuPortfolio", null, "user0001"));
//        teamPortfolioRepository.save(new TeamPortfolio("portfolio02", "yutongxinPortfolio", null, "user0002"));
//        teamPortfolioRepository.save(new TeamPortfolio("portfolio03", "duwenyuanPortfolio", null, "user0003"));
//        teamPortfolioRepository.save(new TeamPortfolio("portfolio04", "hezhiPortfolio", null, "user0004"));
        LOGGER.info("Adding teamPortfolio collection:Done");
    }

    private void initSecurities() {
        LOGGER.info("Initializing security documents and security history documents:...");
        securityRepository.deleteAll();
        securityHistoryRepository.deleteAll();
        RestTemplate restTemplate = new RestTemplate();
        LOGGER.info("--Getting security results");
        SecurityResult securityResult = restTemplate.getForObject("https://api.trochil.cn/v1/usstock/markets?apikey={API_KEY}", SecurityResult.class, API_KEY);
        assert securityResult != null;
        if (securityResult.getStatus().equals("ok")) {
            assert securityResult.getData() != null;
            List<Security> securityList = securityResult.getData();
            securityRepository.saveAll(securityList);

            LOGGER.info("--Getting security history results for securities in portfolio");
            Iterable<Security> portFolioSecurities = securityRepository.findAllById(INIT_SYMBOLS);
            for (Security security :
                    portFolioSecurities) {
                LOGGER.info("----Getting security history results of {}", security.getSymbol());
                String symbol = security.getSymbol();
                String startDate = "2016-01-01";
                SecurityHistoryResult securityHistoryResult = restTemplate.getForObject("https://api.trochil.cn/v1/usstock/history?apikey={API_KEY}&symbol={symbol}&start_date={startDate}", SecurityHistoryResult.class, API_KEY, symbol, startDate);
                assert securityHistoryResult != null;
                assert securityHistoryResult.getData() != null;

                List<SecurityHistory> allSecurityHistoryList = securityHistoryResult.getData();
                security.setHistoryList(allSecurityHistoryList);
                security.setType(Security.TYPE_STOCK);
                allSecurityHistoryList.forEach(securityHistory -> securityHistory.setSymbol(symbol));
                securityHistoryRepository.saveAll(allSecurityHistoryList);
                securityRepository.save(security);
            }
        } else if (securityResult.getStatus().equals("fail")) {
            LOGGER.error("--apikey for hummingbird is exhausted, please change the api key");
        }
        LOGGER.info("Initializing security documents and security history documents: Done");
    }

    //start daily position
    private void initDailyPositions() {
        dailyPositionRepository.deleteAll();
        for (String portfolioId :
                teamPortfolioRepository.findAll().stream().map(TeamPortfolio::getId).collect(Collectors.toList())) {
            this.initDailyPositionsForPortfolio(portfolioId);
        }
    }

    private void initDailyPositionsForPortfolio(String portfolioId) {
        LOGGER.info("Adding daily position documents for portfolio '{}'...", portfolioId);
        for (String symbol :
                INIT_SYMBOLS) {
            List<DailyPosition> dailyPositionList = new ArrayList<>();
            List<SecurityHistory> securityHistoryList = securityHistoryRepository.findAllBySymbol(symbol);
            int count = 0;
            int randomInt = NumUtil.randomInt(100, 10000);
            for (SecurityHistory securityHistory :
                    securityHistoryList) {
                count++;
                if (count % 7 == 0) {
                    randomInt = NumUtil.randomInt(1000, 2000);
                }
                dailyPositionList.add(new DailyPosition(null, portfolioId, securityHistory.getDatetime(), securityHistory, randomInt));
            }
            dailyPositionRepository.saveAll(dailyPositionList);
            LOGGER.info("--adding {} daily position records of symbol: '{}' for portfolio '{}' to collection 'daily_position'", dailyPositionList.size(), symbol, portfolioId);
        }
        LOGGER.info("Adding daily position documents for portfolio '{}': Done", portfolioId);
    }
    //end daily position


    //start transaction
    private void initTransaction() {
        transactionRepository.deleteAll();
        for (String portfolioId :
                teamPortfolioRepository.findAll().stream().map(TeamPortfolio::getId).collect(Collectors.toList())) {
            this.initTransactionForPortfolio(portfolioId);
        }
    }

    private void initTransactionForPortfolio(String portfolioId) {
        LOGGER.info("Adding transaction for portfolio '{}'...", portfolioId);
        for (String symbol :
                INIT_SYMBOLS) {
            LOGGER.info("--{}", symbol);
            List<DailyPosition> dailyPositionList = dailyPositionRepository.findAllBySymbolInPortfolio(portfolioId, symbol);
            dailyPositionList.sort((o1, o2) -> {
                long difference = o1.getDate().getTime() - o2.getDate().getTime();
                if (difference > 0) {
                    return 1;
                } else if (difference < 0) {
                    return -1;
                } else {
                    return 0;
                }
            });

            for (int i = 0; i < dailyPositionList.size(); i++) {
                DailyPosition dailyPosition = dailyPositionList.get(i);
                Transaction transaction = new Transaction();
                if (i == 0) {
                    transaction.setType(Transaction.TYPE_BUY);
                    transaction.setPrice(dailyPosition.getSecurityHistory().getOpen());
                    transaction.setShares(dailyPosition.getShares());
                } else {
                    DailyPosition previousPosition = dailyPositionList.get(i - 1);
                    transaction = this.calculateTransaction(dailyPosition, previousPosition);
                }

                if (null != transaction) {
                    transaction.setDate(dailyPosition.getDate());
                    transaction.setSymbol(dailyPosition.getSecurityHistory().getSymbol());
                    transaction.setPortfolioId(portfolioId);
                    transactionRepository.save(transaction);
                }
            }
        }
        LOGGER.info("Adding transaction for portfolio '{}': Done", portfolioId);
    }

    private Transaction calculateTransaction(DailyPosition dailyPosition, DailyPosition previousPosition) {
        Transaction transaction = new Transaction();
        int shareDiff = dailyPosition.getShares() - previousPosition.getShares();
        if (shareDiff > 0) {
            transaction.setType(Transaction.TYPE_BUY);
            transaction.setPrice(dailyPosition.getSecurityHistory().getOpen());
        } else if (shareDiff < 0) {
            transaction.setType(Transaction.TYPE_SELL);
            transaction.setPrice(previousPosition.getSecurityHistory().getClose());
        } else {
            return null;
        }
        transaction.setShares(shareDiff);
        return transaction;
    }
    //end transaction


    private void initBankAccount() {
        //        2016-01-04T00:00:00.000Z
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (TeamPortfolio portfolio :
                teamPortfolioRepository.findAll()) {

            startCalendar.set(2016, Calendar.JANUARY, 1, 0, 0, 0);
            startCalendar.set(Calendar.MILLISECOND, 0);

            LOGGER.info("setting bankAccount for portfolio '{}'", portfolio.getId());
            Map<Date, Float> historyBalance = new HashMap<>();
            Date today = new Date();
            Calendar calendar = startCalendar;

            float tempBalance = INIT_BALANCE;
            historyBalance.put(calendar.getTime(), tempBalance);
            while (calendar.getTime().before(today)) {
                calendar.add(Calendar.DATE, 1);
                List<Transaction> transactionsOfTheDay = transactionRepository.findTransactionForBankAccountHistoryBalance(portfolio.getId(), calendar.getTime());
                if (transactionsOfTheDay.size() > 0) {
                    for (Transaction transaction :
                            transactionsOfTheDay) {
                        tempBalance = tempBalance + transaction.getPrice() * (-transaction.getShares());
                    }
                    LOGGER.info("--balance on {}: {}", calendar.getTime(), tempBalance);
                    historyBalance.put(calendar.getTime(), tempBalance);
                }
            }

            BankAccount bankAccount = new BankAccount();
            bankAccount.setBalance(tempBalance);
            bankAccount.setBankName("CitiBank");
            bankAccount.setUserId(portfolio.getUserId());
            bankAccount.setHistoryBalance(historyBalance);
            System.out.println(historyBalance.size());
            portfolio.setBankAccount(bankAccount);
            teamPortfolioRepository.save(portfolio);
        }

    }

    @Deprecated
    private void initPortfolioNames() {
        LOGGER.info("Initializing portfolio names...");
        for (Portfolio portfolio :
                portfolioRepository.findAll()) {
            Optional<Security> security = securityRepository.findById(portfolio.getSymbol());
            if (security.isPresent()) {
                portfolio.setName(security.get().getName());
                portfolio.setPurchasePrice(security.get().getHistoryList().get(0).getLow());
                portfolio.setShares(100);
                portfolioRepository.save(portfolio);
            }
        }
        LOGGER.info("Initializing portfolio names: Done");
    }

    @Deprecated
    private void initPortfolio() {
        LOGGER.info("Adding initial portfolio documents to collection 'portfolio'");
        portfolioRepository.deleteAll();
        for (String symbol :
                INIT_SYMBOLS) {
            portfolioRepository.save(new Portfolio("", symbol, 0, 0));
        }
        LOGGER.info("Adding initial portfolio documents to collection 'portfolio': Done");
    }
}
