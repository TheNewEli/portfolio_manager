package neueda.team1.portfolio_manager.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Map;

public class BankAccount {
    @Id
    private String id;
    private String userId;
    private String bankName;
    private float balance;
    private Map<Date, Float> historyBalance;

    public BankAccount(String id, String userId, String bankName, float balance, Map<Date, Float> historyBalance) {
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

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Map<Date, Float> getHistoryBalance() {
        return historyBalance;
    }

    public void setHistoryBalance(Map<Date, Float> historyBalance) {
        this.historyBalance = historyBalance;
    }
}
