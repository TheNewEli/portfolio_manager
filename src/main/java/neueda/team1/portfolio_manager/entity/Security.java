package neueda.team1.portfolio_manager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class Security {
    @Transient
    public static final String TYPE_STOCK = "stock";
    @Transient
    public static final String TYPE_FUTURE = "future";

    @Id
    private String symbol;
    private String name;
    private String exchange;
    private String type;
    private List<SecurityHistory> historyList;

    public Security() {
    }

    public Security(String symbol, String name, String exchange, String type, List<SecurityHistory> historyList) {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
        this.type = type;
        this.historyList = historyList;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SecurityHistory> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<SecurityHistory> historyList) {
        this.historyList = historyList;
    }
}
