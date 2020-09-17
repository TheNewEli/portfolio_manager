package neueda.team1.portfolio_manager.service;

import neueda.team1.portfolio_manager.entity.DailyPosition;
import neueda.team1.portfolio_manager.entity.TeamPortfolio;
import neueda.team1.portfolio_manager.repository.DailyPositionRepository;
import neueda.team1.portfolio_manager.repository.SecurityRepository;
import neueda.team1.portfolio_manager.repository.TeamPortfolioRepository;
import neueda.team1.portfolio_manager.util.DateUtil;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NetWorthService {
    private final DailyPositionRepository dailyPositionRepository;
    private final TeamPortfolioRepository teamPortfolioRepository;

    public static List<List<String>> buildResponseList(Map<Date, Double> map) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        List<String> valueList = map.values().stream().map(num -> String.format("%.2f", num)).collect(Collectors.toList());
        List<String> keyList = map.keySet().stream().map(dateFormat::format).collect(Collectors.toList());
        List<List<String>> response = new ArrayList<>();
        response.add(keyList);
        response.add(valueList);
        return response;
    }

    public NetWorthService(DailyPositionRepository dailyPositionRepository, TeamPortfolioRepository teamPortfolioRepository) {
        this.dailyPositionRepository = dailyPositionRepository;
        this.teamPortfolioRepository = teamPortfolioRepository;
    }

    public Map<Date, Double> getLastDaysNetWorth(String portfolioId, int dayCount) {
        Map<Date, Double> stockValueMap = this.getLastDaysStockValue(portfolioId, dayCount);
        Map<Date, Double> cashValueMap = this.getLastDaysCashValue(portfolioId, dayCount);
        Map<Date, Double> netWorthMap = new TreeMap<>();

        double netWorth;
        for (Date key :
                stockValueMap.keySet()) {

            netWorth = stockValueMap.get(key) + cashValueMap.get(key);
            netWorthMap.put(key, netWorth);
        }
        return netWorthMap;
    }

    public Map<Date, Double> getLastDaysStockValue(String portfolioId, int dayCount) {
        Map<Date, Double> stockValueMap = new TreeMap<>();

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
        Map<Date, Double> stockValueMap = new TreeMap<>();

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
