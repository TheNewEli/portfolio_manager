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


    public Map<Date, Float> getLastWeekNetWorth() {
        return this.getLastDaysNetWorth(7);
    }

    public Map<Date, Float> getLastMonthNetWorth() {
        return this.getLastDaysNetWorth(30);
    }

    public Map<Date, Float> getLastQuarterNetWorth() {
        return this.getLastDaysNetWorth(90);
    }


    public Map<Date, Float> getLastDaysNetWorth(int dayCount) {
        Map<Date, Float> netWorthMap = new HashMap<>();

        List<Portfolio> portfolioList = portfolioRepository.findAll();
        List<String> portfolioSymbols = portfolioList.stream().map(Portfolio::getSymbol).collect(Collectors.toList());
        Iterable<Security> portFolioSecurities = securityRepository.findAllById(portfolioSymbols);
        List<Security> portFolioSecurityList = new ArrayList<>();
        portFolioSecurities.forEach(portFolioSecurityList::add);

        List<List<SecurityHistory>> securityHistoryLists = portFolioSecurityList.stream().map(Security::getHistoryList).collect(Collectors.toList());
        for (List<SecurityHistory> securityHistoryList :
                securityHistoryLists) {
            List<SecurityHistory> lastWeekSecurityHistory = securityHistoryList.stream().filter(single -> !single.getDatetime().before(this.getDateBefore(dayCount))).collect(Collectors.toList());
            for (SecurityHistory securityHistory :
                    lastWeekSecurityHistory) {
                Date historyDate = securityHistory.getDatetime();
                if (null == netWorthMap.get(historyDate)) {
                    netWorthMap.put(historyDate, securityHistory.getClose());
                } else {
                    float current = netWorthMap.get(historyDate);
                    netWorthMap.put(historyDate, current + securityHistory.getClose());
                }
            }
        }
        return netWorthMap;
    }

    private Date getDateBefore(int dayCount) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -dayCount);
        return calendar.getTime();
    }
}
