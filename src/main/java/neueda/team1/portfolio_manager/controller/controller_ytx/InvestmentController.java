package neueda.team1.portfolio_manager.controller.controller_ytx;

import neueda.team1.portfolio_manager.service.service_ytx.httputils_ytx.TrochilHttpManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value="/{symbol}", produces={"application/json"})
    public ResponseEntity<String> getAllPortfolios(@PathVariable String symbol) {
        String result = trochilHttpManage.getInvestmentBySymbol(symbol);
        return ResponseEntity.ok().body(result);
    }
}
