package neueda.team1.portfolio_manager.service.service_ytx.httputils_ytx;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import neueda.team1.portfolio_manager.entity.domain_ytx.Portfolio;
import neueda.team1.portfolio_manager.service.service_ytx.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * TrochilHttpManage class
 * Manage the Trochil API to get data
 * @author Yu Tongxin
 * @date 2020/09/13
 */
@Component
public class TrochilHttpManage {

    private static final String TROCHIL_URL = "https://api.trochil.cn/v1/usstock/quote";
    @Value("apikey=${hummingbird.apikey.5}")
    private String APIKEY_YTX;

    @Autowired
    PortfolioService portfolioService;
    @Autowired
    HttpClient httpClient;

    /**
     *
     * @param symbol symbol of Portfolio
     * @return current related data of
     * the investment(stock or investment) data in Json Format String
     */
    public String getInvestmentBySymbol(String symbol){

        StringBuffer urlBF = new StringBuffer();
        urlBF.append(TROCHIL_URL).append("?");
        urlBF.append(APIKEY_YTX).append("&symbol=").append(symbol);
        HttpHeaders httpHeaders = new HttpHeaders();
        String apiResult =  httpClient.sendGetRequest(urlBF.toString(),httpHeaders);

        JsonElement jsonElement = JsonParser.parseString(apiResult);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if(jsonObject.get("data") == null){
            System.out.println(symbol+" can't be requested");
            return null;
        }
        JsonElement jsonDataElement = jsonObject.get("data");
        JsonObject jsonData = jsonDataElement.getAsJsonObject();
        JsonElement jsonResultElement = jsonData.get(symbol);
        JsonObject jsonResult = jsonResultElement.getAsJsonObject();
        SupplementFromDB(jsonResult,symbol);
        return  jsonResult.toString();
    }

    /**
     * SupplementFromDB, add other field value from DB
     * @param jsonResult  the ultimate return result of
     *                    all information of the investment
     * @param symbol
     */
    public void SupplementFromDB(JsonObject jsonResult, String symbol){
        Optional<Portfolio> result = portfolioService.findById(symbol);

        if(result.isPresent()){
            Portfolio portfolio = result.get();
            jsonResult.addProperty("name",portfolio.getName());
            jsonResult.addProperty("purchasePrice",portfolio.getPurchasePrice());
            jsonResult.addProperty("shares",portfolio.getShares());
            jsonResult.addProperty("currentValue",portfolio.getShares()*jsonResult.get("last").getAsDouble());

        }else{
            System.out.println(symbol+" was not found");
        }
    }


}
