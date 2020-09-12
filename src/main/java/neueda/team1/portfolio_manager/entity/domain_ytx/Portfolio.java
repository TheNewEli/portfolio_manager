package neueda.team1.portfolio_manager.entity.domain_ytx;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
/**
 * Portfolio class
 * POJO
 * @author Yu Tongxin
 * @date 2020/09/12
 */
public class Portfolio {
    @Id
    private String symbol;
    private String name;
    @Field("purchase_price")
    private double purchasePrice;
    private int shares;

    public Portfolio() {
        // Portfolio no-arg constructor.
    }

    public Portfolio(String name, String symbol, double purchasePrice, int shares) {
        this.name = name;
        this.symbol = symbol;
        this.purchasePrice = purchasePrice;
        this.shares = shares;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", shares=" + shares +
                '}';
    }
}
