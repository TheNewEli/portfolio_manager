package neueda.team1.portfolio_manager.repository;

import neueda.team1.portfolio_manager.entity.Security;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SecurityRepository extends MongoRepository<Security, String> {

}
