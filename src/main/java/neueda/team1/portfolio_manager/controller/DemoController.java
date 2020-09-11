package neueda.team1.portfolio_manager.controller;

import neueda.team1.portfolio_manager.entity.User;
import neueda.team1.portfolio_manager.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class DemoController {
    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("get")
    public User demoGet() {
        return demoService.getUser();
    }
}
