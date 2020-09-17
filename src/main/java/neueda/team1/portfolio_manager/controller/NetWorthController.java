package neueda.team1.portfolio_manager.controller;

import neueda.team1.portfolio_manager.service.NetWorthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("netWorth")
public class NetWorthController {
    private static final String PORTFOLIO_ID = "portfolio01";
    private final NetWorthService netWorthService;

    public NetWorthController(NetWorthService netWorthService) {
        this.netWorthService = netWorthService;
    }

    @GetMapping(value = "/{period}", produces = {"application/json"})
    public ResponseEntity<List<List<String>>> getNetWorth(@PathVariable String period) {
        Map<Date, Double> netWortMap;
        switch (period) {
            case "week" -> netWortMap = netWorthService.getLastDaysNetWorth(PORTFOLIO_ID, 7);
            case "month" -> netWortMap = netWorthService.getLastDaysNetWorth(PORTFOLIO_ID, 30);
            case "quarter" -> netWortMap = netWorthService.getLastDaysNetWorth(PORTFOLIO_ID, 90);
            default -> {
                int dayCount = Integer.parseInt(period);
                netWortMap = netWorthService.getLastDaysNetWorth(PORTFOLIO_ID, dayCount);
            }
        }
        return ResponseEntity.ok().body(NetWorthService.buildResponseList(netWortMap));
    }

    @GetMapping(value = "cash/{period}", produces = {"application/json"})
    public ResponseEntity<List<List<String>>> getCashValue(@PathVariable String period) {
        ResponseEntity<List<List<String>>> netWorthResponse = this.getNetWorth(period);
        ResponseEntity<List<List<String>>> stockValueResponse = this.getStockValue(period);

        List<String> dateList = netWorthResponse.getBody().get(0);
        List<String> netWorthList = netWorthResponse.getBody().get(1);
        List<String> stockValueList = stockValueResponse.getBody().get(1);

        List<String> cashValueList = new ArrayList<>();
        double netWorth = 0;
        double stockValue = 0;
        double cashValue = 0;
        for (int i = 0; i < netWorthList.size(); i++) {
            netWorth = Double.valueOf(netWorthList.get(i));
            stockValue = Double.valueOf(stockValueList.get(i));
            cashValue = netWorth - stockValue;
            cashValueList.add(String.format("%.2f", cashValue));
        }

        List<List<String>> response = new ArrayList<>();
        response.add(dateList);
        response.add(cashValueList);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "stock/{period}", produces = {"application/json"})
    public ResponseEntity<List<List<String>>> getStockValue(@PathVariable String period) {
        Map<Date, Double> stockValueMap;
        switch (period) {
            case "week" -> stockValueMap = netWorthService.getLastDaysStockValue(PORTFOLIO_ID, 7);
            case "month" -> stockValueMap = netWorthService.getLastDaysStockValue(PORTFOLIO_ID, 30);
            case "quarter" -> stockValueMap = netWorthService.getLastDaysStockValue(PORTFOLIO_ID, 90);
            default -> {
                int dayCount = Integer.parseInt(period);
                stockValueMap = netWorthService.getLastDaysStockValue(PORTFOLIO_ID, dayCount);
            }
        }

        return ResponseEntity.ok().body(NetWorthService.buildResponseList(stockValueMap));
    }

//    @GetMapping(value = "netWorth/days/{dayCount}", produces = {"application/json"})
//    public ResponseEntity<Map<Date, Double>> getNetWorthByDayCount(@PathVariable int dayCount) {
//        Map<Date, Double> netWortList = netWorthService.getLastDaysNetWorth(PORTFOLIO_ID, dayCount);
//        return ResponseEntity.ok().body(netWortList);
//    }
//
//    @GetMapping(value = "cashValue/days/{dayCount}", produces = {"application/json"})
//    public ResponseEntity<Map<Date, Double>> getCashValueByDayCount(@PathVariable int dayCount) {
//        Map<Date, Double> netWortList = netWorthService.getLastDaysCashValue(PORTFOLIO_ID, dayCount);
//        return ResponseEntity.ok().body(netWortList);
//    }
//
//    @GetMapping(value = "stockValue/days/{dayCount}", produces = {"application/json"})
//    public ResponseEntity<Map<Date, Double>> getStockValueByDayCount(@PathVariable int dayCount) {
//        Map<Date, Double> netWortList = netWorthService.getLastDaysStockValue(PORTFOLIO_ID, dayCount);
//        return ResponseEntity.ok().body(netWortList);
//    }

}
