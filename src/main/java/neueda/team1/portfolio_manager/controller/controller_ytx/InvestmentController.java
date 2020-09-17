package neueda.team1.portfolio_manager.controller.controller_ytx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import neueda.team1.portfolio_manager.entity.DailyPosition;
import neueda.team1.portfolio_manager.entity.SecurityHistory;
import neueda.team1.portfolio_manager.entity.TeamPortfolio;
import neueda.team1.portfolio_manager.repository.DailyPositionRepository;
import neueda.team1.portfolio_manager.repository.TeamPortfolioRepository;
import neueda.team1.portfolio_manager.service.service_ytx.PortfolioService;
import neueda.team1.portfolio_manager.service.service_ytx.httputils_ytx.TrochilHttpManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * InvestmentController class
 * GET more detailed information about Portfolio, using Trochil API
 * for real-time Financial data of specific symbol
 * @author Yu Tongxin
 * @date 2020/09/13
 */
@CrossOrigin
@RestController
@RequestMapping("/investmentinfo")
public class InvestmentController {

    @Autowired
    private TeamPortfolioRepository teamPortfolioRepository;

    @Autowired
    private DailyPositionRepository dailyPositionRepository;

    @Autowired
    private TrochilHttpManage trochilHttpManage;

    @Autowired
    PortfolioService portfolioService;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @GetMapping(value="", produces={"application/json"})
    public ResponseEntity<String> getAllInvestment() {

       // List<TeamPortfolio>portfolios = teamPortfolioRepository.findAll();
        JsonArray jsonResultArray = new JsonArray();
        //for(TeamPortfolio portfolio:portfolios){
            Page<DailyPosition> positions = getLatestPositions();
            for(DailyPosition dp:positions){
                LOGGER.info("DailyPosition Id:"+dp.getId());
                JsonElement jsonElement= supplementInvestment(dp);
                if(null == jsonElement){
                    continue;
                }
                jsonResultArray.add(jsonElement);
            }
        //}
        return ResponseEntity.ok().body(jsonResultArray.toString());
    }

    @GetMapping(value="/test", produces={"application/json"})
    public ResponseEntity<String> getAllInvestmentForTest() {
        return ResponseEntity.ok().body("[{\"symbol\":\"EBAY\",\"bid\":52.27,\"ask\":52.29,\"bid_qty\":15,\"ask_qty\":3,\"datetime\":\"2020-09-15 03:59:59\",\"last\":52.3,\"open\":53.285,\"high\":53.58,\"low\":52.21,\"volume\":7476320.0,\"pre_close\":52.77,\"timestamp\":1600114635587.0,\"change\":-0.47,\"percent_change\":-0.89,\"name\":\"eBay\",\"purchasePrice\":26.079999923706055,\"shares\":100,\"currentValue\":5230.0},{\"symbol\":\"ZNGA\",\"bid\":8.38,\"ask\":8.39,\"bid_qty\":398,\"ask_qty\":308,\"datetime\":\"2020-09-15 03:59:55\",\"last\":8.39,\"open\":8.4,\"high\":8.49,\"low\":8.32,\"volume\":10646367.0,\"pre_close\":8.35,\"timestamp\":1600114821154.0,\"change\":0.04,\"percent_change\":0.48,\"name\":\"Zynga\",\"purchasePrice\":2.5299999713897705,\"shares\":100,\"currentValue\":839.0},{\"symbol\":\"YELP\",\"bid\":21.92,\"ask\":21.93,\"bid_qty\":4,\"ask_qty\":1,\"datetime\":\"2020-09-15 03:59:57\",\"last\":21.93,\"open\":21.71,\"high\":22.01,\"low\":21.45,\"volume\":1079677.0,\"pre_close\":21.46,\"timestamp\":1600114625265.0,\"change\":0.47,\"percent_change\":2.19,\"name\":\"Yelp\",\"purchasePrice\":27.049999237060547,\"shares\":100,\"currentValue\":2193.0},{\"symbol\":\"YNDX\",\"bid\":62.57,\"ask\":62.58,\"bid_qty\":1,\"ask_qty\":29,\"datetime\":\"2020-09-15 03:59:59\",\"last\":62.58,\"open\":62.15,\"high\":62.99,\"low\":62.06,\"volume\":1399568.0,\"pre_close\":61.82,\"timestamp\":1600114998721.0,\"change\":0.76,\"percent_change\":1.23,\"name\":\"Yandex Nv\",\"purchasePrice\":14.510000228881836,\"shares\":100,\"currentValue\":6258.0},{\"symbol\":\"SPWR\",\"bid\":10.96,\"ask\":10.97,\"bid_qty\":59,\"ask_qty\":10,\"datetime\":\"2020-09-15 03:59:57\",\"last\":10.97,\"open\":10.75,\"high\":10.99,\"low\":10.35,\"volume\":4531028.0,\"pre_close\":10.64,\"timestamp\":1600114924497.0,\"change\":0.33,\"percent_change\":3.1,\"name\":\"Sunpower Corp\",\"purchasePrice\":28.81999969482422,\"shares\":100,\"currentValue\":1097.0},{\"symbol\":\"SSYS\",\"bid\":14.21,\"ask\":14.23,\"bid_qty\":1,\"ask_qty\":11,\"datetime\":\"2020-09-15 03:59:58\",\"last\":14.21,\"open\":14.41,\"high\":14.68,\"low\":14.15,\"volume\":604897.0,\"pre_close\":14.31,\"timestamp\":1600113600730.0,\"change\":-0.1,\"percent_change\":-0.7,\"name\":\"Stratasys\",\"purchasePrice\":23.08209991455078,\"shares\":100,\"currentValue\":1421.0},{\"symbol\":\"SPLK\",\"bid\":183.55,\"ask\":183.67,\"bid_qty\":1,\"ask_qty\":10,\"datetime\":\"2020-09-15 03:59:59\",\"last\":183.67,\"open\":188.87,\"high\":189.82,\"low\":182.82,\"volume\":2565360.0,\"pre_close\":186.93,\"timestamp\":1600114635569.0,\"change\":-3.26,\"percent_change\":-1.74,\"name\":\"Splunk Inc\",\"purchasePrice\":56.279998779296875,\"shares\":100,\"currentValue\":18367.0},{\"symbol\":\"SAP\",\"bid\":160.55,\"ask\":160.7,\"bid_qty\":21,\"ask_qty\":10,\"datetime\":\"2020-09-15 03:59:59\",\"last\":160.7,\"open\":160.91,\"high\":161.625,\"low\":159.84,\"volume\":782485.0,\"pre_close\":159.57,\"timestamp\":1600114200001.0,\"change\":1.13,\"percent_change\":0.71,\"name\":\"SAP\",\"purchasePrice\":75.88999938964844,\"shares\":100,\"currentValue\":16069.999999999998},{\"symbol\":\"RP\",\"bid\":56.39,\"ask\":56.47,\"bid_qty\":5,\"ask_qty\":15,\"datetime\":\"2020-09-15 03:59:55\",\"last\":56.46,\"open\":56.03,\"high\":56.75,\"low\":55.93,\"volume\":632805.0,\"pre_close\":55.69,\"timestamp\":1600113616978.0,\"change\":0.77,\"percent_change\":1.38,\"name\":\"Realpage Inc\",\"purchasePrice\":21.485000610351562,\"shares\":100,\"currentValue\":5646.0},{\"symbol\":\"PANW\",\"bid\":245.75,\"ask\":245.78,\"bid_qty\":5,\"ask_qty\":15,\"datetime\":\"2020-09-15 03:59:59\",\"last\":245.73,\"open\":246.0,\"high\":247.85,\"low\":244.09,\"volume\":2230367.0,\"pre_close\":242.94,\"timestamp\":1600113660140.0,\"change\":2.79,\"percent_change\":1.15,\"name\":\"Palo Alto Networks Inc\",\"purchasePrice\":169.1999969482422,\"shares\":100,\"currentValue\":24573.0},{\"symbol\":\"LN\",\"bid\":50.63,\"ask\":50.71,\"bid_qty\":50,\"ask_qty\":1,\"datetime\":\"2020-09-15 03:59:59\",\"last\":50.71,\"open\":50.63,\"high\":50.71,\"low\":50.6,\"volume\":21196.0,\"pre_close\":50.67,\"timestamp\":1600113601900.0,\"change\":0.04,\"percent_change\":0.08,\"name\":\"Line Corp\",\"purchasePrice\":40.599998474121094,\"shares\":100,\"currentValue\":5071.0},{\"symbol\":\"IRBT\",\"bid\":79.67,\"ask\":79.81,\"bid_qty\":1,\"ask_qty\":32,\"datetime\":\"2020-09-15 03:59:58\",\"last\":79.81,\"open\":77.85,\"high\":79.81,\"low\":77.06,\"volume\":395239.0,\"pre_close\":77.02,\"timestamp\":1600113610349.0,\"change\":2.79,\"percent_change\":3.62,\"name\":\"Irobot Corp\",\"purchasePrice\":33.529998779296875,\"shares\":100,\"currentValue\":7981.0},{\"symbol\":\"ILMN\",\"bid\":353.39,\"ask\":353.65,\"bid_qty\":2,\"ask_qty\":4,\"datetime\":\"2020-09-15 03:59:58\",\"last\":353.65,\"open\":354.69,\"high\":358.96,\"low\":352.16,\"volume\":753352.0,\"pre_close\":349.93,\"timestamp\":1600114634646.0,\"change\":3.72,\"percent_change\":1.06,\"name\":\"Illumina Inc\",\"purchasePrice\":179.8800048828125,\"shares\":100,\"currentValue\":35365.0},{\"symbol\":\"IBM\",\"bid\":122.07,\"ask\":122.1,\"bid_qty\":57,\"ask_qty\":65,\"datetime\":\"2020-09-15 03:59:59\",\"last\":122.09,\"open\":122.36,\"high\":123.38,\"low\":121.76,\"volume\":7260664.0,\"pre_close\":121.46,\"timestamp\":1600115208679.0,\"change\":0.63,\"percent_change\":0.52,\"name\":\"IBM\",\"purchasePrice\":134.24000549316406,\"shares\":100,\"currentValue\":12209.0},{\"symbol\":\"GRPN\",\"bid\":28.47,\"ask\":28.48,\"bid_qty\":6,\"ask_qty\":1,\"datetime\":\"2020-09-15 03:59:57\",\"last\":28.48,\"open\":32.47,\"high\":32.96,\"low\":28.22,\"volume\":4097781.0,\"pre_close\":32.39,\"timestamp\":1600115372089.0,\"change\":-3.91,\"percent_change\":-12.07,\"name\":\"Groupon Inc\",\"purchasePrice\":58.400001525878906,\"shares\":100,\"currentValue\":2848.0},{\"symbol\":\"GDOT\",\"bid\":50.47,\"ask\":50.52,\"bid_qty\":1,\"ask_qty\":10,\"datetime\":\"2020-09-15 03:59:52\",\"last\":50.45,\"open\":50.13,\"high\":51.015,\"low\":49.52,\"volume\":685466.0,\"pre_close\":49.56,\"timestamp\":1600113622194.0,\"change\":0.89,\"percent_change\":1.8,\"name\":\"Green Dot Corp\",\"purchasePrice\":15.970000267028809,\"shares\":100,\"currentValue\":5045.0},{\"symbol\":\"GOGO\",\"bid\":9.25,\"ask\":9.26,\"bid_qty\":28,\"ask_qty\":1,\"datetime\":\"2020-09-15 04:00:00\",\"last\":9.27,\"open\":9.45,\"high\":9.59,\"low\":9.08,\"volume\":2723132.0,\"pre_close\":9.45,\"timestamp\":1600115374952.0,\"change\":-0.18,\"percent_change\":-1.9,\"name\":\"Gogo Inc\",\"purchasePrice\":16.850000381469727,\"shares\":100,\"currentValue\":927.0},{\"symbol\":\"FEYE\",\"bid\":12.62,\"ask\":12.63,\"bid_qty\":129,\"ask_qty\":72,\"datetime\":\"2020-09-15 03:59:54\",\"last\":12.62,\"open\":12.61,\"high\":12.72,\"low\":12.545,\"volume\":1609317.0,\"pre_close\":12.49,\"timestamp\":1600114680794.0,\"change\":0.13,\"percent_change\":1.04,\"name\":\"Fireeye Inc\",\"purchasePrice\":20.280000686645508,\"shares\":100,\"currentValue\":1262.0},{\"symbol\":\"FB\",\"bid\":266.12,\"ask\":266.13,\"bid_qty\":1,\"ask_qty\":1,\"datetime\":\"2020-09-15 03:59:59\",\"last\":266.15,\"open\":270.95,\"high\":276.64,\"low\":265.7,\"volume\":24060802.0,\"pre_close\":266.61,\"timestamp\":1600115374415.0,\"change\":-0.46,\"percent_change\":-0.17,\"name\":\"Facebook Inc\",\"purchasePrice\":99.75,\"shares\":100,\"currentValue\":26614.999999999996},{\"symbol\":\"ENV\",\"bid\":77.82,\"ask\":77.92,\"bid_qty\":1,\"ask_qty\":34,\"datetime\":\"2020-09-15 03:59:57\",\"last\":77.91,\"open\":76.6,\"high\":78.18,\"low\":76.212,\"volume\":612236.0,\"pre_close\":75.64,\"timestamp\":1600113874171.0,\"change\":2.27,\"percent_change\":3.0,\"name\":\"Envestnet Inc\",\"purchasePrice\":28.270000457763672,\"shares\":100,\"currentValue\":7791.0},{\"symbol\":\"BYND\",\"bid\":143.31,\"ask\":143.37,\"bid_qty\":1,\"ask_qty\":8,\"datetime\":\"2020-09-15 03:59:59\",\"last\":143.18,\"open\":136.15,\"high\":145.37,\"low\":136.05,\"volume\":4842500.0,\"pre_close\":134.88,\"timestamp\":1600114714736.0,\"change\":8.3,\"percent_change\":6.15,\"name\":\"Beyond Meat Inc\",\"purchasePrice\":45.0,\"shares\":100,\"currentValue\":14318.0},{\"symbol\":\"Y\",\"bid\":539.06,\"ask\":540.45,\"bid_qty\":1,\"ask_qty\":3,\"datetime\":\"2020-09-15 03:59:57\",\"last\":539.0,\"open\":549.45,\"high\":557.53,\"low\":539.0,\"volume\":123982.0,\"pre_close\":549.57,\"timestamp\":1600113613510.0,\"change\":-10.57,\"percent_change\":-1.92,\"name\":\"Alleghany Corp\",\"purchasePrice\":466.5,\"shares\":100,\"currentValue\":53900.0},{\"symbol\":\"ADBE\",\"bid\":485.68,\"ask\":486.0,\"bid_qty\":8,\"ask_qty\":1,\"datetime\":\"2020-09-15 03:59:59\",\"last\":485.91,\"open\":484.99,\"high\":490.44,\"low\":476.87,\"volume\":4090536.0,\"pre_close\":471.35,\"timestamp\":1600115304412.0,\"change\":14.56,\"percent_change\":3.09,\"name\":\"Adobe Inc\",\"purchasePrice\":90.54000091552734,\"shares\":100,\"currentValue\":48591.0},{\"symbol\":\"T\",\"bid\":29.13,\"ask\":29.14,\"bid_qty\":41,\"ask_qty\":102,\"datetime\":\"2020-09-15 03:59:59\",\"last\":29.14,\"open\":29.1,\"high\":29.3,\"low\":29.0,\"volume\":54860671.0,\"pre_close\":29.0,\"timestamp\":1600115346784.0,\"change\":0.14,\"percent_change\":0.48,\"name\":\"AT&T\",\"purchasePrice\":34.0,\"shares\":100,\"currentValue\":2914.0},{\"symbol\":\"MMM\",\"bid\":168.44,\"ask\":168.57,\"bid_qty\":35,\"ask_qty\":185,\"datetime\":\"2020-09-15 03:59:59\",\"last\":168.46,\"open\":167.66,\"high\":169.425,\"low\":166.425,\"volume\":4673301.0,\"pre_close\":166.17,\"timestamp\":1600114647883.0,\"change\":2.29,\"percent_change\":1.38,\"name\":\"3M Co\",\"purchasePrice\":145.39999389648438,\"shares\":100,\"currentValue\":16846.0}]");
    }

    @GetMapping(value="/{symbol}", produces={"application/json"})
    public ResponseEntity<String> getInvestmentBySymbol(@PathVariable String symbol) {
        LOGGER.info("querying for : symbol:"+symbol);
        Page<DailyPosition> positions=getLatestPositions();
        DailyPosition position = null;
        for(DailyPosition d:positions){
            LOGGER.info("querying for : positionID:"+d.getId());
            if(d.getSecurityHistory().getSymbol().equals(symbol)){
                position = d;
                break;
            }
        }
        try {
            JsonElement jsonElement = supplementInvestment(position);
            return ResponseEntity.ok().body(jsonElement.toString());
        }catch (NullPointerException e){
            return ResponseEntity.ok().body("{}");
        }

    }

    private Page<DailyPosition>getLatestPositions(){
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(0,24, sort);
        return dailyPositionRepository.findAll(pageable);
    }


    private JsonElement supplementInvestment(DailyPosition dp){
        LOGGER.info("querying for :portfolioId:"+dp.getPortfolioId());
        SecurityHistory securityHistory = dp.getSecurityHistory();
        String investment = trochilHttpManage.getInvestmentBySymbol(securityHistory.getSymbol());
        if(null == investment){return null;}
        JsonElement jsonElement = JsonParser.parseString(investment);
        JsonObject jsonResult = jsonElement.getAsJsonObject();
        jsonResult.addProperty("shares",dp.getShares());
        jsonResult.addProperty("currentValue",dp.getShares()*jsonResult.get("last").getAsDouble());
        return jsonElement;
    }

}
