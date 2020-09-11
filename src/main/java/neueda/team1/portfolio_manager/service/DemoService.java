package neueda.team1.portfolio_manager.service;

import neueda.team1.portfolio_manager.entity.User;
import neueda.team1.portfolio_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoService {
    @Autowired
    private UserRepository userRepository;

    public User getUser() {
        return userRepository.findAll().get(0);
    }
}
