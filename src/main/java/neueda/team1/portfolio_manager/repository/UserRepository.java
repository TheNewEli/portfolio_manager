package neueda.team1.portfolio_manager.repository;

import neueda.team1.portfolio_manager.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
