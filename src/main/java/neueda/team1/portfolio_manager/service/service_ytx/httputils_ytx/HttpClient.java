package neueda.team1.portfolio_manager.service.service_ytx.httputils_ytx;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
/**
 * TrochilHttpManage class
 * Manage the HTTP GET/POST to Request for data
 * @author Yu Tongxin
 * @date 2020/09/13
 */
@Service
public class HttpClient {
    /**
     * GET Request to the target URL
     * @param headers can be set from external methods
     * @return  String response from the url server
     */
    public String sendGetRequest(String url, HttpHeaders headers){

        RestTemplate client = new RestTemplate();
        HttpMethod method = HttpMethod.GET;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * POST Request to the target URL
     * @param url       target url
     * @param params    related params
     * @param headers can be set from external methods
     * @return  response from the url server
     */
    public String sendPostRequest(String url, MultiValueMap<String, String> params,  HttpHeaders headers){
        RestTemplate client = new RestTemplate();
        HttpMethod method = HttpMethod.POST;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = client.exchange(url, method, requestEntity,String .class);
        return response.getBody();
    }
}
