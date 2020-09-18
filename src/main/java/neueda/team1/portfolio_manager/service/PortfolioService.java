package neueda.team1.portfolio_manager.service;

import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import neueda.team1.portfolio_manager.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * PortfolioService class
 * MongoDB Data Access Service
 * @author Yu Tongxin
 * @date 2020/09/12
 */
@Service
public class PortfolioService {
    @Autowired
    PortfolioRepository repository;

    public List<Portfolio>findAll(){
        return repository.findAll();
    }

    public Page<Portfolio> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Optional<Portfolio> findById(String id){
        return  repository.findById(id);
    }

    public Portfolio insert(Portfolio portfolio){
        return repository.insert(portfolio);
    }

    public Portfolio update(Portfolio portfolio){
        return repository.save(portfolio);
    }

    public void delete(Portfolio portfolio){
        repository.delete(portfolio);
    }

}
