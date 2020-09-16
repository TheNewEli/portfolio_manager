package neueda.team1.portfolio_manager.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Map;

public class BankAccount {
    @Id
    private String id;
    private String userId;
    private String bankName;
    private Double balance;
    private Map<Date, Double> historyBalance;

    public BankAccount(String id, String userId, String bankName, double balance, Map<Date, Double> historyBalance) {
        this.id = id;
        this.userId = userId;
        this.bankName = bankName;
        this.balance = balance;
        this.historyBalance = historyBalance;
    }

    public BankAccount() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Map<Date, Double> getHistoryBalance() {
        return historyBalance;
    }

    public void setHistoryBalance(Map<Date, Double> historyBalance) {
        this.historyBalance = historyBalance;
    }
}
