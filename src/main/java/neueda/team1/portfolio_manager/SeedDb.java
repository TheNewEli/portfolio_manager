package neueda.team1.portfolio_manager;

import neueda.team1.portfolio_manager.entity.Security;
import neueda.team1.portfolio_manager.entity.SecurityHistory;
import neueda.team1.portfolio_manager.entity.SecurityHistoryResult;
import neueda.team1.portfolio_manager.entity.SecurityResult;
import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import neueda.team1.portfolio_manager.repository.SecurityRepository;
import neueda.team1.portfolio_manager.repository.repository_ytx.PortfolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SeedDb {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${hummingbird.apikey.2}")
    private String API_KEY;

    private final PortfolioRepository portfolioRepository;
    private final SecurityRepository securityRepository;

    public SeedDb(PortfolioRepository portfolioRepository, SecurityRepository securityRepository) {
        this.portfolioRepository = portfolioRepository;
        this.securityRepository = securityRepository;
    }

    @PostConstruct
    public void initDb() {
        this.initPortfolio();
        this.initSecurities();
    }

    private void initPortfolio() {
        LOGGER.info("Adding 10 initial portfolio documents");
        portfolioRepository.deleteAll();
        portfolioRepository.save(new Portfolio("Apple Inc.", "AAPL", 112, 100));
        portfolioRepository.save(new Portfolio("Nikola Corporation", "NKLA", 32.13, 100));
        portfolioRepository.save(new Portfolio("Peloton Interactive", "PTON", 84.04, 100));
        portfolioRepository.save(new Portfolio("General Electric Company", "GE", 5.95, 100));
        portfolioRepository.save(new Portfolio("Tesla, Inc.", "TSLA", 372.72, 100));
        portfolioRepository.save(new Portfolio("Ford Motor Company", "F", 7, 100));
        portfolioRepository.save(new Portfolio("Advanced Micro Devices, Inc.", "AMD", 76.34, 100));
        portfolioRepository.save(new Portfolio("NIO Limited", "NIO", 17.97, 100));
        portfolioRepository.save(new Portfolio("Bank of America", "BAC", 25.5, 100));
        portfolioRepository.save(new Portfolio("Vale S.A.", "VALE", 11.67, 100));
    }

    private void initSecurities() {
        LOGGER.info("Initializing stock documents:");
        RestTemplate restTemplate = new RestTemplate();
        LOGGER.info("\tGetting security results");
        SecurityResult securityResult = restTemplate.getForObject("https://api.trochil.cn/v1/usstock/markets?apikey={API_KEY}", SecurityResult.class, API_KEY);
        assert securityResult != null;
        List<Security> securityList = securityResult.getData();
        securityRepository.saveAll(securityList);

        LOGGER.info("\tGetting security history results for securities in portfolio");
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        Set<String> portfolioSymbols = portfolioList.stream().map(portfolio -> portfolio.getSymbol()).collect(Collectors.toSet());
        Iterable<Security> portFolioSecurities = securityRepository.findAllById(portfolioSymbols);
        for (Security security :
                portFolioSecurities) {
            LOGGER.info("\t\tGetting security history results of {}", security.getSymbol());
            String symbol = security.getSymbol();
            String startDate = "2016-01-01";
            SecurityHistoryResult securityHistoryResult = restTemplate.getForObject("https://api.trochil.cn/v1/usstock/history?apikey={API_KEY}&symbol={symbol}&start_date={startDate}", SecurityHistoryResult.class, API_KEY, symbol, startDate);
            assert securityHistoryResult != null;
            List<SecurityHistory> allSecurityHistoryList = securityHistoryResult.getData();
            security.setHistoryList(allSecurityHistoryList);
            security.setType(Security.TYPE_STOCK);
            securityRepository.save(security);
        }
    }


}
