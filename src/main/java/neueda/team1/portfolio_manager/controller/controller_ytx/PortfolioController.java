package neueda.team1.portfolio_manager.controller.controller_ytx;

import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import neueda.team1.portfolio_manager.service.service_ytx.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

/**
 * PortfolioController class
 * @author Yu Tongxin
 * @date 2020/09/12
 */
@CrossOrigin
@RestController
@RequestMapping("/investment")
public class PortfolioController {
    @Autowired
    PortfolioService portfolioService;

    // Get all portfolios.
    @GetMapping(value="/portfolios", produces={"application/json"})
    public ResponseEntity<Collection<Portfolio>> getAllPortfolios() {
        Collection<Portfolio> result = portfolioService.findAll();
        return ResponseEntity.ok().body(result);
    }

    // Get a specific portfolio.
    @GetMapping(value="/portfolio/{symbol}", produces={"application/json"})
    public ResponseEntity<Portfolio> getItem(@PathVariable String symbol) {
        Optional<Portfolio> result = portfolioService.findById(symbol);
        return result.map(portfolio -> ResponseEntity.ok().body(portfolio)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Insert a new portfolio.
    @PostMapping(value="/portfolio",
            consumes={"application/json"},
            produces={"application/json"})
    public ResponseEntity<Portfolio> addItem(@RequestBody Portfolio portfolio) {
        portfolioService.insert(portfolio);
        URI uri = URI.create("/portfolio/" + portfolio.getSymbol());
        return ResponseEntity.created(uri).body(portfolio);
    }

    // Update an existing portfolio
    @PutMapping(value="/portfolio/{symbol}", consumes={"application/json"})
    public ResponseEntity modifyItem(@PathVariable String symbol, @RequestBody Portfolio portfolio) {
        if(portfolioService.findById(symbol).isPresent()){
            portfolioService.update(portfolio);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete an existing portfolio.
    @DeleteMapping("/portfolio/{symbol}")
    public ResponseEntity deleteItem(@PathVariable String symbol) {
        if(portfolioService.findById(symbol).isPresent()){
            Portfolio portfolio = portfolioService.findById(symbol).get();
            portfolioService.delete(portfolio);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}
