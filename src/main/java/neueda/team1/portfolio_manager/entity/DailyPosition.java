package neueda.team1.portfolio_manager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("daily_position")
public class DailyPosition {
    @Id
    private String id;
    private String portfolio_id;
    private Date date;
    @Field("security_history")
    private SecurityHistory securityHistory;
    private int shares;

    public DailyPosition(String id, String portfolio_id, Date date, SecurityHistory securityHistory, int shares) {
        this.id = id;
        this.portfolio_id = portfolio_id;
        this.date = date;
        this.securityHistory = securityHistory;
        this.shares = shares;
    }

    public DailyPosition() {
    }

    public String getPortfolio_id() {
        return portfolio_id;
    }

    public void setPortfolio_id(String portfolio_id) {
        this.portfolio_id = portfolio_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SecurityHistory getSecurityHistory() {
        return securityHistory;
    }

    public void setSecurityHistory(SecurityHistory securityHistory) {
        this.securityHistory = securityHistory;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
