package neueda.team1.portfolio_manager.repository;

import neueda.team1.portfolio_manager.entity.DailyPosition;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DailyPositionRepository extends MongoRepository<DailyPosition, String> {
}
