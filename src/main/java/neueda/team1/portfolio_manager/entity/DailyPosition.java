package neueda.team1.portfolio_manager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("daily_position")
public class DailyPosition {
    @Id
    private String id;
    @Field("portfolio_id")
    private String portfolioId;
    private Date date;
    @Field("security_history")
    private SecurityHistory securityHistory;
    private int shares;

    public DailyPosition(String id, String portfolioId, Date date, SecurityHistory securityHistory, int shares) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.date = date;
        this.securityHistory = securityHistory;
        this.shares = shares;
    }

    public DailyPosition() {
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
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
