package neueda.team1.portfolio_manager.repository;

import neueda.team1.portfolio_manager.entity.SecurityHistory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SecurityHistoryRepository extends MongoRepository<SecurityHistory, String> {
    @Query("{'symbol': '?0'}}")
    List<SecurityHistory> findAllBySymbol(String symbol);
}
