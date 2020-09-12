package neueda.team1.portfolio_manager.service.service_ytx;

import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.sound.sampled.Port;
import java.util.List;

@SpringBootTest
public class PortfolioServiceTest {

    @Autowired
    PortfolioService portfolioService;

    @Test
    void listFindAllTest(){
        List<Portfolio>portfolios = portfolioService.findAll();
        System.out.println(portfolios.size()+" items:");
        for(Portfolio p:portfolios){
            System.out.println(p.toString());
        }
    }

    @Test
    void pageFindAllTest(){
        Sort sort = Sort.by(Sort.Direction.DESC, "symbol");
        Pageable pageable = PageRequest.of(1,3, sort);
        Page<Portfolio>portfolios = portfolioService.findAll(pageable);
        for(Portfolio p:portfolios){
            System.out.println(p.toString());
        }
    }

    @Test
    void findById(){

    }

}
