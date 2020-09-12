package neueda.team1.portfolio_manager.service.service_ytx;

import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import neueda.team1.portfolio_manager.repository.repository_ytx.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
/**
 * SeedPortfolio class
 * Portfolio Data init
 * @author Yu Tongxin
 * @date 2020/09/12
 */
@Component
public class SeedPortfolio {
    @Autowired
    PortfolioRepository repository;

    @PostConstruct
    public void init(){

        repository.save(new Portfolio("Apple Inc.", "AAPL", 112,  100));
        repository.save(new Portfolio("Nikola Corporation", "NKLA", 32.13,  100));
        repository.save(new Portfolio("Peloton Interactive", "PTON", 84.04,  100));
        repository.save(new Portfolio("General Electric Company", "GE", 5.95,  100));
        repository.save(new Portfolio("Tesla, Inc.", "TSLA", 372.72,  100));
        repository.save(new Portfolio("Ford Motor Company", "F", 7,  100));
        repository.save(new Portfolio("Advanced Micro Devices, Inc.", "AMD", 76.34,  100));
        repository.save(new Portfolio("NIO Limited", "NIO", 17.97,  100));
        repository.save(new Portfolio("Bank of America", "BAC", 25.5,  100));
        repository.save(new Portfolio("Vale S.A.",  "VALE", 11.67, 100));
    }

}
