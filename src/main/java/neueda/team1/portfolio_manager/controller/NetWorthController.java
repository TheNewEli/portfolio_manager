package neueda.team1.portfolio_manager.controller;

import neueda.team1.portfolio_manager.service.NetWorthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("netWorth")
public class NetWorthController {
    private final NetWorthService netWorthService;

    public NetWorthController(NetWorthService netWorthService) {
        this.netWorthService = netWorthService;
    }

    @GetMapping(value = "{period}", produces = {"application/json"})
    public ResponseEntity<Map<Date, Float>> getAllPortfolios(@PathVariable String period) {
        Map<Date, Float> netWortList = new HashMap<>();
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
                int dayCount = Integer.parseInt(period);
                break;
        }
        return ResponseEntity.ok().body(netWortList);
    }

    @GetMapping(value = "/days/{dayCount}", produces = {"application/json"})
    public ResponseEntity<Map<Date, Float>> getAllPortfolios(@PathVariable int dayCount) {
        Map<Date, Float> netWortList = netWorthService.getLastDaysNetWorth(dayCount);
        return ResponseEntity.ok().body(netWortList);
    }

}
