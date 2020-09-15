package neueda.team1.portfolio_manager.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("daily_position")
public class DailyPosition {
    private String portfolio_id;
    private Date date;
    private SecurityHistory securityHistory;
    private int shares;

    public DailyPosition(String portfolio_id, Date date, SecurityHistory securityHistory, int shares) {
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
}
