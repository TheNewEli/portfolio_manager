package neueda.team1.portfolio_manager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("team_portfolio")
public class TeamPortfolio {
    @Id
    private String id;
    private String name;
    private float cash;

    public TeamPortfolio() {
    }

    public TeamPortfolio(String id, String name, float cash) {
        this.id = id;
        this.name = name;
        this.cash = cash;
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

    public float getCash() {
        return cash;
    }

    public void setCash(float cash) {
        this.cash = cash;
    }
}
