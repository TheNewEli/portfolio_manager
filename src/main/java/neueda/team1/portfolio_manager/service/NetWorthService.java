package neueda.team1.portfolio_manager.service;

import neueda.team1.portfolio_manager.entity.DailyPosition;
import neueda.team1.portfolio_manager.entity.TeamPortfolio;
import neueda.team1.portfolio_manager.repository.DailyPositionRepository;
import neueda.team1.portfolio_manager.repository.SecurityRepository;
import neueda.team1.portfolio_manager.repository.TeamPortfolioRepository;
import neueda.team1.portfolio_manager.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NetWorthService {
    private final SecurityRepository securityRepository;
    private final DailyPositionRepository dailyPositionRepository;
    private final TeamPortfolioRepository teamPortfolioRepository;

    public NetWorthService(SecurityRepository securityRepository, DailyPositionRepository dailyPositionRepository, TeamPortfolioRepository teamPortfolioRepository) {
        this.securityRepository = securityRepository;
        this.dailyPositionRepository = dailyPositionRepository;
        this.teamPortfolioRepository = teamPortfolioRepository;
    }


    public Map<Date, Double> getLastWeekNetWorth(String portfolioId) {
        return this.getLastDaysNetWorth(portfolioId, 7);
    }

    public Map<Date, Double> getLastMonthNetWorth(String portfolioId) {
        return this.getLastDaysNetWorth(portfolioId, 30);
    }

    public Map<Date, Double> getLastQuarterNetWorth(String portfolioId) {
        return this.getLastDaysNetWorth(portfolioId, 90);
    }

    public Map<Date, Double> getLastWeekCashValue(String portfolioId) {
        return this.getLastDaysCashValue(portfolioId, 7);
    }

    public Map<Date, Double> getLastMonthCashValue(String portfolioId) {
        return this.getLastDaysCashValue(portfolioId, 30);
    }

    public Map<Date, Double> getLastQuarterCashValue(String portfolioId) {
        return this.getLastDaysCashValue(portfolioId, 90);
    }

    public Map<Date, Double> getLastWeekStockValue(String portfolioId) {
        return this.getLastDaysStockValue(portfolioId, 7);
    }

    public Map<Date, Double> getLastMonthStockValue(String portfolioId) {
        return this.getLastDaysStockValue(portfolioId, 30);
    }

    public Map<Date, Double> getLastQuarterStockValue(String portfolioId) {
        return this.getLastDaysStockValue(portfolioId, 90);
    }

    public Map<Date, Double> getLastDaysNetWorth(String portfolioId, int dayCount) {
        Map<Date, Double> stockValueMap = this.getLastDaysStockValue(portfolioId, dayCount);
        Map<Date, Double> cashValueMap = this.getLastDaysCashValue(portfolioId, dayCount);
        Map<Date, Double> netWorthMap = new HashMap<>();

        double netWorth;
        for (Date key :
                stockValueMap.keySet()) {

            netWorth = stockValueMap.get(key) + cashValueMap.get(key);
            netWorthMap.put(key, netWorth);
        }
        return netWorthMap;
    }

    public Map<Date, Double> getLastDaysStockValue(String portfolioId, int dayCount) {
        Map<Date, Double> stockValueMap = new HashMap<>();

        Date today = DateUtil.getToday();
        Date startDay = DateUtil.getDateBeforeToday(dayCount);

        Optional<TeamPortfolio> portfolio = teamPortfolioRepository.findById(portfolioId);
        if (portfolio.isPresent()) {
            //stock value
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDay);
            while (calendar.getTime().before(today)) {
                calendar.add(Calendar.DATE, 1);
                List<DailyPosition> dailyPositionsOfTheDay = dailyPositionRepository.findAllByDateAndPortfolioId(portfolioId, calendar.getTime());
                if (dailyPositionsOfTheDay.size() > 0) {
                    double stockValue = dailyPositionsOfTheDay.stream().mapToDouble(DailyPosition::getValue).sum();
                    stockValueMap.put(calendar.getTime(), stockValue);
                }
            }
        }
        return stockValueMap;
    }

    public Map<Date, Double> getLastDaysCashValue(String portfolioId, int dayCount) {
        Map<Date, Double> stockValueMap = new HashMap<>();

        Date today = DateUtil.getToday();
        Date startDay = DateUtil.getDateBeforeToday(dayCount);

        Optional<TeamPortfolio> portfolio = teamPortfolioRepository.findById(portfolioId);
        if (portfolio.isPresent()) {
            Map<Date, Double> cashHistoryMap = portfolio.get().getBankAccount().getHistoryBalance();
            //stock value
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDay);
            while (calendar.getTime().before(today)) {
                calendar.add(Calendar.DATE, 1);
                stockValueMap.put(calendar.getTime(), getCash(cashHistoryMap, calendar.getTime()));
            }
        }
        return stockValueMap;
    }

    private double getCash(Map<Date, Double> cashHistoryMap, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        double cash = 0;
        Double tempCash = null;
        while ((cash == 0) && (tempCash == null)) {
            calendar.add(Calendar.DATE, -1);
            tempCash = cashHistoryMap.get(calendar.getTime());
            if (tempCash != null) {
                cash = tempCash;
            }
        }
        return cash;
    }

//    public Map<Date, Float> getLastDaysNetWorth(int dayCount) {
//        Map<Date, Float> netWorthMap = new HashMap<>();
//
//        List<Portfolio> portfolioList = portfolioRepository.findAll();
//        List<String> portfolioSymbols = portfolioList.stream().map(Portfolio::getSymbol).collect(Collectors.toList());
//        Iterable<Security> portFolioSecurities = securityRepository.findAllById(portfolioSymbols);
//        List<Security> portFolioSecurityList = new ArrayList<>();
//        portFolioSecurities.forEach(portFolioSecurityList::add);
//
//        List<List<SecurityHistory>> securityHistoryLists = portFolioSecurityList.stream().map(Security::getHistoryList).collect(Collectors.toList());
//        for (List<SecurityHistory> securityHistoryList :
//                securityHistoryLists) {
//            List<SecurityHistory> lastWeekSecurityHistory = securityHistoryList.stream().filter(single -> !single.getDatetime().before(this.getDateBefore(dayCount))).collect(Collectors.toList());
//            for (SecurityHistory securityHistory :
//                    lastWeekSecurityHistory) {
//                Date historyDate = securityHistory.getDatetime();
//                if (null == netWorthMap.get(historyDate)) {
//                    netWorthMap.put(historyDate, securityHistory.getClose());
//                } else {
//                    float current = netWorthMap.get(historyDate);
//                    netWorthMap.put(historyDate, current + securityHistory.getClose());
//                }
//            }
//        }
//        return netWorthMap;
//    }
}
