package neueda.team1.portfolio_manager.repository;

import neueda.team1.portfolio_manager.entity.TeamPortfolio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamPortfolioRepository extends MongoRepository<TeamPortfolio, String> {
}
