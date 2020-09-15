package neueda.team1.portfolio_manager;

import neueda.team1.portfolio_manager.entity.*;
import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import neueda.team1.portfolio_manager.repository.BankAccountRepository;
import neueda.team1.portfolio_manager.repository.SecurityRepository;
import neueda.team1.portfolio_manager.repository.UserRepository;
import neueda.team1.portfolio_manager.repository.repository_ytx.PortfolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SeedDb {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${hummingbird.apikey.2}")
    private String API_KEY;

    private final PortfolioRepository portfolioRepository;
    private final SecurityRepository securityRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public SeedDb(PortfolioRepository portfolioRepository, SecurityRepository securityRepository,
                  UserRepository userRepository, BankAccountRepository bankAccountRepository) {
        this.portfolioRepository = portfolioRepository;
        this.securityRepository = securityRepository;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @PostConstruct
    public void initDb() {
        this.initPortfolio();
        this.initSecurities(); // Comment out this line if your database is already initiated
        this.initPortfolioNames();
        this.initUser();
        this.initBankAccount();
    }

    private void initBankAccount() {
        bankAccountRepository.deleteAll();
        bankAccountRepository.save(new BankAccount("1", 12007000.0, "Citibank", "1", new Date()));
        bankAccountRepository.save(new BankAccount("2", 13006000.0, "Wells Fargo", "1", new Date()));
        bankAccountRepository.save(new BankAccount("3", 14005000.0, "China Bank", "1", new Date()));

    }

    private void initUser() {
        userRepository.deleteAll();
        userRepository.save(new User("1", "caoyu", "pwd", "caoyu@neueda.com"));
        userRepository.save(new User("2", "yutongxin", "pwd", "yutongxin@neueda.com"));
        userRepository.save(new User("3", "duwenyuan", "pwd", "duwenyuan@neueda.com"));
        userRepository.save(new User("4", "hezhi", "pwd", "hezhi@neueda.com"));
    }

    private void initPortfolioNames() {
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
    }

    private void initPortfolio() {
        LOGGER.info("Adding 20 initial portfolio documents");
        portfolioRepository.deleteAll();
        portfolioRepository.save(new Portfolio("", "EBAY", 0, 0));
        portfolioRepository.save(new Portfolio("", "ZNGA", 0, 0));
        portfolioRepository.save(new Portfolio("", "YELP", 0, 0));
        portfolioRepository.save(new Portfolio("", "YNDX", 0, 0));
        portfolioRepository.save(new Portfolio("", "SPWR", 0, 0));
        portfolioRepository.save(new Portfolio("", "SSYS", 0, 0));
        portfolioRepository.save(new Portfolio("", "SPLK", 0, 0));
        portfolioRepository.save(new Portfolio("", "SAP", 0, 0));
        portfolioRepository.save(new Portfolio("", "RP", 0, 0));
        portfolioRepository.save(new Portfolio("", "PANW", 0, 0));
        portfolioRepository.save(new Portfolio("", "LN", 0, 0));
        portfolioRepository.save(new Portfolio("", "IRBT", 0, 0));
        portfolioRepository.save(new Portfolio("", "ILMN", 0, 0));
        portfolioRepository.save(new Portfolio("", "IBM", 0, 0));
        portfolioRepository.save(new Portfolio("", "GRPN", 0, 0));
        portfolioRepository.save(new Portfolio("", "GDOT", 0, 0));
        portfolioRepository.save(new Portfolio("", "GOGO", 0, 0));
        portfolioRepository.save(new Portfolio("", "FEYE", 0, 0));
        portfolioRepository.save(new Portfolio("", "FB", 0, 0));
        portfolioRepository.save(new Portfolio("", "FEYE", 0, 0));
        portfolioRepository.save(new Portfolio("", "ENV", 0, 0));
        portfolioRepository.save(new Portfolio("", "BYND", 0, 0));
        portfolioRepository.save(new Portfolio("", "Y", 0, 0));
        portfolioRepository.save(new Portfolio("", "ADBE", 0, 0));
        portfolioRepository.save(new Portfolio("", "T", 0, 0));
        portfolioRepository.save(new Portfolio("", "MMM", 0, 0));
    }

    private void initSecurities() {
        LOGGER.info("Initializing stock documents:");
        RestTemplate restTemplate = new RestTemplate();
        LOGGER.info("--Getting security results");
        SecurityResult securityResult = restTemplate.getForObject("https://api.trochil.cn/v1/usstock/markets?apikey={API_KEY}", SecurityResult.class, API_KEY);
        assert securityResult != null;
        if (securityResult.getStatus().equals("ok")) {
            assert securityResult.getData() != null;
            List<Security> securityList = securityResult.getData();
            securityRepository.saveAll(securityList);

            LOGGER.info("--Getting security history results for securities in portfolio");
            List<Portfolio> portfolioList = portfolioRepository.findAll();
            Set<String> portfolioSymbols = portfolioList.stream().map(Portfolio::getSymbol).collect(Collectors.toSet());
            Iterable<Security> portFolioSecurities = securityRepository.findAllById(portfolioSymbols);
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
                securityRepository.save(security);
            }
        } else if (securityResult.getStatus().equals("fail")) {
            LOGGER.error("--apikey for hummingbird is exhausted, please change the api key");
        }
    }
}
