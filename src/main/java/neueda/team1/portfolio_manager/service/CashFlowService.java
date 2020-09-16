package neueda.team1.portfolio_manager.service;

import neueda.team1.portfolio_manager.entity.TeamPortfolio;
import neueda.team1.portfolio_manager.entity.Transaction;
import neueda.team1.portfolio_manager.repository.TeamPortfolioRepository;
import neueda.team1.portfolio_manager.repository.TransactionRepository;
import neueda.team1.portfolio_manager.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CashFlowService {
    private final TeamPortfolioRepository teamPortfolioRepository;
    private final TransactionRepository transactionRepository;

    public CashFlowService(TeamPortfolioRepository teamPortfolioRepository, TransactionRepository transactionRepository) {
        this.teamPortfolioRepository = teamPortfolioRepository;
        this.transactionRepository = transactionRepository;
    }

    public Map<String, Double> getLastDaysSpending(String portfolioId, int dayCount) {
        Date startDate = DateUtil.getDateBeforeToday(dayCount);

        Map<String, Double> spendingMap = new HashMap<>();
        Optional<TeamPortfolio> teamPortfolio = teamPortfolioRepository.findById(portfolioId);
        if (teamPortfolio.isPresent()) {
            List<Transaction> transactionList = transactionRepository.findAllByPortfolioId(portfolioId);
            List<Transaction> filteredTransactionList = transactionList.stream()
                    .filter(transaction -> (transaction.getDate().after(startDate) && transaction.getBanceChange() < 0))
                    .collect(Collectors.toList());
            spendingMap = this.calculateSpending(filteredTransactionList);
        }
        return spendingMap;
    }

    public Map<String, Double> getLastDaysIncome(String portfolioId, int dayCount) {
        Date startDate = DateUtil.getDateBeforeToday(dayCount);
        Map<String, Double> spendingMap = new HashMap<>();

        Optional<TeamPortfolio> teamPortfolio = teamPortfolioRepository.findById(portfolioId);
        if (teamPortfolio.isPresent()) {
            List<Transaction> transactionList = transactionRepository.findAllByPortfolioId(portfolioId);
            List<Transaction> filteredTransactionList = transactionList.stream()
                    .filter(transaction -> (transaction.getDate().after(startDate) && transaction.getBanceChange() > 0))
                    .collect(Collectors.toList());
            spendingMap = this.calculateSpending(filteredTransactionList);
        }
        return spendingMap;
    }

    private Map<String, Double> calculateSpending(List<Transaction> transactionList) {
        Map<String, Double> spendingMap = new HashMap<>();

        String symbol;
        Double spending;
        for (Transaction transaction :
                transactionList) {
            symbol = transaction.getSymbol();
            spending = spendingMap.get(symbol);
            if (spending == null) {
                spending = transaction.getBanceChange();
            } else {
                spending = transaction.getBanceChange() + spending;
            }
            spendingMap.put(symbol, spending);
        }
        return spendingMap;
    }
}
