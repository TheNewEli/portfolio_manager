package neueda.team1.portfolio_manager.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("security_history")
public class SecurityHistory {
    private Date datetime;
    private Float open;
    private Float high;
    private Float low;
    private Float close;
    private Float volume;
    private String symbol;

    public SecurityHistory(Date datetime, Float open, Float high, Float low, Float close, Float volume, String symbol) {
        this.datetime = datetime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.symbol = symbol;
    }

    public SecurityHistory() {
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Float getOpen() {
        return open;
    }

    public void setOpen(Float open) {
        this.open = open;
    }

    public Float getHigh() {
        return high;
    }

    public void setHigh(Float high) {
        this.high = high;
    }

    public Float getLow() {
        return low;
    }

    public void setLow(Float low) {
        this.low = low;
    }

    public Float getClose() {
        return close;
    }

    public void setClose(Float close) {
        this.close = close;
    }

    public Float getVolume() {
        return volume;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }

    public String getSymbol() {
        return symbol;
    }


    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
