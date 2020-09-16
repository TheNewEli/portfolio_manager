package neueda.team1.portfolio_manager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class Transaction {
    @Id
    private String id;
    @Field("portfolio_id")
    private String portfolioId;
    private Date date;
    private String type;
    private String symbol;
    private int shares;
    private Double price;

    @Transient
    public static final String TYPE_BUY = "buy";

    @Transient
    public static final String TYPE_SELL = "sell";

    public Transaction() {
    }

    public Transaction(String id, String portfolioId, Date date, String type, String symbol, int shares, Double price) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.date = date;
        this.type = type;
        this.symbol = symbol;
        this.shares = shares;
        this.price = price;
    }

    public Double getBanceChange() {
        return -1 * this.getShares() * this.getPrice();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
