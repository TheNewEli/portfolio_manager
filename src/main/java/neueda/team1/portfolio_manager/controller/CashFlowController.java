package neueda.team1.portfolio_manager.controller;

import neueda.team1.portfolio_manager.service.CashFlowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("cashFlow")
public class CashFlowController {
    private static final String PORTFOLIO_ID = "portfolio01";
    private final CashFlowService cashFlowService;

    public CashFlowController(CashFlowService cashFlowService) {
        this.cashFlowService = cashFlowService;
    }

    @GetMapping(value = "{period}", produces = {"application/json"})
    public ResponseEntity<List<List<String>>> getCashFlow(@PathVariable String period) {
        Map<String, Double> incomeMap = this.getIncomeMap(period).getBody();
        Map<String, Double> spendingMap = this.getSpendingMap(period).getBody();

        List<String> symbolList = new ArrayList<>(spendingMap.keySet());
        List<String> incomeList = incomeMap.values().stream().map(num -> String.format("%.2f", num)).collect(Collectors.toList());
        List<String> spendingList = spendingMap.values().stream().map(num -> String.format("%.2f", num)).collect(Collectors.toList());

        List<List<String>> response = new ArrayList<>();
        response.add(symbolList);
        response.add(incomeList);
        response.add(spendingList);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "spend/{period}", produces = {"application/json"})
    public ResponseEntity<Map<String, Double>> getSpendingMap(@PathVariable String period) {
        Map<String, Double> spendingMap;
        switch (period) {
            case "week" -> spendingMap = cashFlowService.getLastDaysSpending(PORTFOLIO_ID, 7);
            case "month" -> spendingMap = cashFlowService.getLastDaysSpending(PORTFOLIO_ID, 30);
            case "quarter" -> spendingMap = cashFlowService.getLastDaysSpending(PORTFOLIO_ID, 90);
            default -> {
                int dayCount = Integer.parseInt(period);
                spendingMap = cashFlowService.getLastDaysSpending(PORTFOLIO_ID, dayCount);
            }
        }
        return ResponseEntity.ok().body(spendingMap);
    }

    @GetMapping(value = "income/{period}", produces = {"application/json"})
    public ResponseEntity<Map<String, Double>> getIncomeMap(@PathVariable String period) {
        Map<String, Double> incomeMap;
        switch (period) {
            case "week" -> incomeMap = cashFlowService.getLastDaysIncome(PORTFOLIO_ID, 7);
            case "month" -> incomeMap = cashFlowService.getLastDaysIncome(PORTFOLIO_ID, 30);
            case "quarter" -> incomeMap = cashFlowService.getLastDaysIncome(PORTFOLIO_ID, 90);
            default -> {
                int dayCount = Integer.parseInt(period);
                incomeMap = cashFlowService.getLastDaysIncome(PORTFOLIO_ID, dayCount);
            }
        }
        return ResponseEntity.ok().body(incomeMap);
    }

    @GetMapping(value = "/test", produces = {"application/json"})
    public ResponseEntity<String> getAllCashAccountForTest() {
        return ResponseEntity.ok().body("[{\"bank\":\"Citi Bank\", \"type\":\"Checking\", \"value\":2552.77, \"timestamp\":1600114635587.0}, {\"bank\":\"Citi Bank\", \"type\":\"Savings\", \"value\":1206.00, \"timestamp\":1600114635587.0}]");
    }
}
