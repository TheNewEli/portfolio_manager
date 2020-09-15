package neueda.team1.portfolio_manager.repository;

import neueda.team1.portfolio_manager.entity.DailyPosition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface DailyPositionRepository extends MongoRepository<DailyPosition, String> {
    @Query("{'portfolio_id' : ?0, 'security_history.symbol':?1}}}")
    public List<DailyPosition> findAllBySymbolInPortfolio(String portfolioId, String symbol);

    @Query("{'portfolio_id' : ?0, 'security_history.symbol':?1, 'date':{2}}}}")
    public List<DailyPosition> findOneBySymbolAndDateInPortfolio(String portfolioId, String symbol, Date date);
}
