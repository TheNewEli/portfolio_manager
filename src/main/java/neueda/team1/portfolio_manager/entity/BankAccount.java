package neueda.team1.portfolio_manager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("bank_account")
public class BankAccount {
    @Id
    private String id;
    private double balance;
    @Field("bank_name")
    private String bankName;
    @Field("user_id")
    private String userId;
    @Field("lastModified")
    private Date lastModified;

    public BankAccount() {
    }

    public BankAccount(String id, double balance, String bank_name, String userId, Date lastModified) {
        this.id = id;
        this.balance = balance;
        this.bankName = bank_name;
        this.userId = userId;
        this.lastModified = lastModified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }


}
