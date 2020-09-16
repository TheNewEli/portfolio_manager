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

    @Autowired
    Date zeroToday;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @GetMapping(value="", produces={"application/json"})
    public ResponseEntity<String> getAllInvestment() {

       // List<TeamPortfolio>portfolios = teamPortfolioRepository.findAll();
        JsonArray jsonResultArray = new JsonArray();
        //for(TeamPortfolio portfolio:portfolios){
            Page<DailyPosition> positions = getLatestPositions();
            for(DailyPosition dp:positions){
                LOGGER.info("DailyPosition Date:"+dp.getDate());
                JsonElement jsonElement= supplementInvestment(dp);
                if(null == jsonElement){
                    continue;
                }
                jsonResultArray.add(jsonElement);
            }
        //}
        return ResponseEntity.ok().body(jsonResultArray.toString());
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
