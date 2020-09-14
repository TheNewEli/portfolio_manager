package neueda.team1.portfolio_manager.controller.controller_ytx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import neueda.team1.portfolio_manager.service.service_ytx.PortfolioService;
import neueda.team1.portfolio_manager.service.service_ytx.httputils_ytx.TrochilHttpManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * InvestmentController class
 * GET more detailed information about Portfolio, using Trochil API
 * for real-time Financial data of specific symbol
 * PortfolioController should be used for portfolio CRUD
 * InvestmentController should be used for more specific information
 * @author Yu Tongxin
 * @date 2020/09/13
 */
@CrossOrigin
@RestController
@RequestMapping("/investmentinfo")
public class InvestmentController {

    @Autowired
    private TrochilHttpManage trochilHttpManage;

    @Autowired
    PortfolioService portfolioService;

    @GetMapping(value="/{symbol}", produces={"application/json"})
    public ResponseEntity<String> getInvestmentBySymbol(@PathVariable String symbol) {
        String result = trochilHttpManage.getInvestmentBySymbol(symbol);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value="", produces={"application/json"})
    public ResponseEntity<String> getAllInvestment() {

        List<Portfolio>portfolios = portfolioService.findAll();
        JsonArray jsonResultArray = new JsonArray();
        for(Portfolio portfolio:portfolios){
            String investment = trochilHttpManage.getInvestmentBySymbol(portfolio.getSymbol());
            JsonElement jsonElement = JsonParser.parseString(investment);
            jsonResultArray.add(jsonElement);
        }
        return ResponseEntity.ok().body(jsonResultArray.toString());
    }


}
