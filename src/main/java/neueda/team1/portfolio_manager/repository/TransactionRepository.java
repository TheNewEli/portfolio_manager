package neueda.team1.portfolio_manager.repository;

import neueda.team1.portfolio_manager.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
