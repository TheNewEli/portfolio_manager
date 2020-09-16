package neueda.team1.portfolio_manager.controller;

import neueda.team1.portfolio_manager.service.NetWorthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("")
public class NetWorthController {
    private static final String PORTFOLIO_ID = "portfolio01";
    private final NetWorthService netWorthService;

    public NetWorthController(NetWorthService netWorthService) {
        this.netWorthService = netWorthService;
    }

    @GetMapping(value = "netWorth/{period}", produces = {"application/json"})
    public ResponseEntity<Map<Date, Double>> getNetWorth(@PathVariable String period) {
        Map<Date, Double> netWortList;
        switch (period) {
            case "week" -> netWortList = netWorthService.getLastWeekNetWorth(PORTFOLIO_ID);
            case "month" -> netWortList = netWorthService.getLastMonthNetWorth(PORTFOLIO_ID);
            case "quarter" -> netWortList = netWorthService.getLastQuarterNetWorth(PORTFOLIO_ID);
            default -> {
                int dayCount = Integer.parseInt(period);
                netWortList = this.getNetWorthByDayCount(dayCount).getBody();
            }
        }
        return ResponseEntity.ok().body(netWortList);
    }

    @GetMapping(value = "cashValue/{period}", produces = {"application/json"})
    public ResponseEntity<Map<Date, Double>> getCashValue(@PathVariable String period) {
        Map<Date, Double> cashValueList = new HashMap<>();
        switch (period) {
            case "week":
                cashValueList = netWorthService.getLastWeekCashValue(PORTFOLIO_ID);
                break;
            case "month":
                cashValueList = netWorthService.getLastMonthCashValue(PORTFOLIO_ID);
                break;
            case "quarter":
                cashValueList = netWorthService.getLastQuarterCashValue(PORTFOLIO_ID);
                break;
            default:
                int dayCount = Integer.parseInt(period);
                break;
        }
        return ResponseEntity.ok().body(cashValueList);
    }

    @GetMapping(value = "stockValue/{period}", produces = {"application/json"})
    public ResponseEntity<Map<Date, Double>> getStockValue(@PathVariable String period) {
        Map<Date, Double> stockValueMap = new HashMap<>();
        switch (period) {
            case "week":
                stockValueMap = netWorthService.getLastWeekStockValue(PORTFOLIO_ID);
                break;
            case "month":
                stockValueMap = netWorthService.getLastMonthStockValue(PORTFOLIO_ID);
                break;
            case "quarter":
                stockValueMap = netWorthService.getLastQuarterStockValue(PORTFOLIO_ID);
                break;
            default:
                int dayCount = Integer.parseInt(period);
                break;
        }
        return ResponseEntity.ok().body(stockValueMap);
    }

    @GetMapping(value = "netWorth/days/{dayCount}", produces = {"application/json"})
    public ResponseEntity<Map<Date, Double>> getNetWorthByDayCount(@PathVariable int dayCount) {
        Map<Date, Double> netWortList = netWorthService.getLastDaysNetWorth(PORTFOLIO_ID, dayCount);
        return ResponseEntity.ok().body(netWortList);
    }

    @GetMapping(value = "cashValue/days/{dayCount}", produces = {"application/json"})
    public ResponseEntity<Map<Date, Double>> getCashValueByDayCount(@PathVariable int dayCount) {
        Map<Date, Double> netWortList = netWorthService.getLastDaysCashValue(PORTFOLIO_ID, dayCount);
        return ResponseEntity.ok().body(netWortList);
    }

    @GetMapping(value = "stockValue/days/{dayCount}", produces = {"application/json"})
    public ResponseEntity<Map<Date, Double>> getStockValueByDayCount(@PathVariable int dayCount) {
        Map<Date, Double> netWortList = netWorthService.getLastDaysStockValue(PORTFOLIO_ID, dayCount);
        return ResponseEntity.ok().body(netWortList);
    }

}
