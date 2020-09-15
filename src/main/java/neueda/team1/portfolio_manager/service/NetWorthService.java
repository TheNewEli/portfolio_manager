package neueda.team1.portfolio_manager.service;

import neueda.team1.portfolio_manager.entity.Security;
import neueda.team1.portfolio_manager.entity.SecurityHistory;
import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import neueda.team1.portfolio_manager.repository.SecurityRepository;
import neueda.team1.portfolio_manager.repository.repository_ytx.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NetWorthService {
    private final SecurityRepository securityRepository;
    private final PortfolioRepository portfolioRepository;

    public NetWorthService(SecurityRepository securityRepository, PortfolioRepository portfolioRepository) {
        this.securityRepository = securityRepository;
        this.portfolioRepository = portfolioRepository;
    }


    public List<Float> getLastWeekNetWorth() {
        return this.getLastDaysNetWorth(7);
    }

    public List<Float> getLastMonthNetWorth() {
        return this.getLastDaysNetWorth(30);
    }

    public List<Float> getLastQuarterNetWorth() {
        return this.getLastDaysNetWorth(90);
    }


    public List<Float> getLastDaysNetWorth(int dayCount) {
        List<Float> netWorthList = new ArrayList<>();

        List<Portfolio> portfolioList = portfolioRepository.findAll();
        List<String> portfolioSymbols = portfolioList.stream().map(Portfolio::getSymbol).collect(Collectors.toList());
        Iterable<Security> portFolioSecurities = securityRepository.findAllById(portfolioSymbols);
        List<Security> portFolioSecurityList = new ArrayList<>();
        portFolioSecurities.forEach(portFolioSecurityList::add);

        List<List<SecurityHistory>> securityHistoryLists = portFolioSecurityList.stream().map(Security::getHistoryList).collect(Collectors.toList());
        for (List<SecurityHistory> securityHistoryList :
                securityHistoryLists) {
            List<SecurityHistory> lastWeekSecurityHistory = securityHistoryList.stream().filter(single -> !single.getDatetime().before(this.getDateBefore(dayCount))).collect(Collectors.toList());
            List<Float> lastWeekClosePrice = lastWeekSecurityHistory.stream().map(SecurityHistory::getClose).collect(Collectors.toList());
            for (int i = 0; i < lastWeekClosePrice.size(); i++) {
                if (netWorthList.size() < i + 1) {
                    netWorthList.add(lastWeekClosePrice.get(i));
                } else {
                    float current = netWorthList.get(i);
                    netWorthList.set(i, current + lastWeekClosePrice.get(i));
                }
            }
        }
        return netWorthList;
    }

    private Date getDateBefore(int dayCount) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -dayCount);
        return calendar.getTime();
    }
}
