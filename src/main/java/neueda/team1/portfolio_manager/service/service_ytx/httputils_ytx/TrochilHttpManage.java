package neueda.team1.portfolio_manager.service.service_ytx.httputils_ytx;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * TrochilHttpManage class
 * Manage the Trochil API to get data
 * @author Yu Tongxin
 * @date 2020/09/13
 */
@Component
public class TrochilHttpManage {

    private static final String TROCHIL_URL = "https://api.trochil.cn/v1/usstock/quote";
    @Value("apikey=${hummingbird.apikey}")
    private String APIKEY_YTX;

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

        return  jsonResult.toString();
    }

}
