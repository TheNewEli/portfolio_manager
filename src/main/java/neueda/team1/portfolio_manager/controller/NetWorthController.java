package neueda.team1.portfolio_manager.controller;

import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import neueda.team1.portfolio_manager.service.NetWorthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("netWorth")
public class NetWorthController {
    private final NetWorthService netWorthService;

    public NetWorthController(NetWorthService netWorthService) {
        this.netWorthService = netWorthService;
    }

    @GetMapping(value = "{period}", produces = {"application/json"})
    public ResponseEntity<Collection<Float>> getAllPortfolios(@PathVariable String period) {
        List<Float> netWortList = new ArrayList<>();
        switch (period) {
            case "week":
                netWortList = netWorthService.getLastWeekNetWorth();
                break;
            case "month":
                netWortList = netWorthService.getLastMonthNetWorth();
                break;
            case "quarter":
                netWortList = netWorthService.getLastQuarterNetWorth();
                break;
            default:
                break;
        }
        return ResponseEntity.ok().body(netWortList);
    }

}
