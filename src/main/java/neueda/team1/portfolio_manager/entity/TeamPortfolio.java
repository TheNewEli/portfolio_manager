package neueda.team1.portfolio_manager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document("team_portfolio")
public class TeamPortfolio {
    @Id
    private String id;
    private String name;
    @Field("bank_account")
    private BankAccount bankAccount;
    @Field("user_id")
    private String userId;

    public TeamPortfolio() {
    }

    public TeamPortfolio(String id, String name, BankAccount bankAccount, String userId) {
        this.id = id;
        this.name = name;
        this.bankAccount = bankAccount;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
