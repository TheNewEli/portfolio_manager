package neueda.team1.portfolio_manager.controller;

import neueda.team1.portfolio_manager.service.CashFlowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("cashFlow")
public class CashFlowController {
    private static final String PORTFOLIO_ID = "portfolio01";
    private final CashFlowService cashFlowService;

    public CashFlowController(CashFlowService cashFlowService) {
        this.cashFlowService = cashFlowService;
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
}
