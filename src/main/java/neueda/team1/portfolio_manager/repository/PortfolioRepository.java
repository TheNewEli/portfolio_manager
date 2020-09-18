package neueda.team1.portfolio_manager.repository;

import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;
/**
 * PortfolioRepository class
 * MongoDB Data Access Interface
 * @author Yu Tongxin
 * @date 2020/09/12
 */
public interface PortfolioRepository extends MongoRepository<Portfolio, String> {

}
