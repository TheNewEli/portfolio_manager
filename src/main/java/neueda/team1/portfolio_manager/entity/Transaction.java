package neueda.team1.portfolio_manager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.Date;

public class Transaction {
    @Id
    private String id;
    private Date date;
    private String type;
    private String symbol;
    private int shares;
    private float price;

    @Transient
    public static String TYPE_BUY = "buy";

    @Transient
    public static String TYPE_SELL = "sell";

    public Transaction() {
    }

    public Transaction(String id, Date date, String type, String symbol, int shares, int price) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.symbol = symbol;
        this.shares = shares;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
