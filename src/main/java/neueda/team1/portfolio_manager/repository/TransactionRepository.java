package neueda.team1.portfolio_manager.repository;

import neueda.team1.portfolio_manager.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    @Query("{'portfolio_id':?0,'date': ?1}")
    public List<Transaction> findTransactionForBankAccountHistoryBalance(String portfolioId, Date Date);

    @Query("{'portfolio_id':?0}}")
    public List<Transaction> findAllByPortfolioId(String portfolioId);
}
