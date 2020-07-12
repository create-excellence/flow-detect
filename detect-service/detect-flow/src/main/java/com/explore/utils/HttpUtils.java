package com.explore.utils;

import com.explore.common.ServerResponse;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author PinTeh
 * @date 2020/7/12
 */
public class HttpUtils {

    public static ServerResponse post(String url, MultiValueMap<String, String> params){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<ServerResponse> response = client.exchange(url, method, requestEntity, ServerResponse.class);

        return response.getBody();
    }
}
