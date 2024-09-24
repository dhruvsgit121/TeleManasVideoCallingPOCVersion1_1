package com.example.ehrc.telemanas.Service.NewServices;

import com.example.ehrc.telemanas.Utilities.BitlyRequest;
import com.example.ehrc.telemanas.Utilities.BitlyResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;

public class BitlyService {

    private static final String BITLY_URL = "https://api-ssl.bitly.com/v4/shorten";

    public static String shortenUrl(String longUrl, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        // Create a request payload
        BitlyRequest request = new BitlyRequest(longUrl);

        // Set authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<BitlyRequest> entity = new HttpEntity<>(request, headers);
        BitlyResponse response = restTemplate.postForObject(BITLY_URL, entity, BitlyResponse.class);

        return response != null ? response.getLink() : null;
    }


//    @Value("${bitly.access.token}")
//    private String accessToken;
//
//    private final RestTemplate restTemplate;
//
//    public BitlyService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public String shortenUrl(String longUrl) {
//        String url = "https://api-ssl.bitly.com/v4/shorten";
//
//        // Create a request payload
//        BitlyRequest request = new BitlyRequest(longUrl);
//
//        // Set authorization header
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + accessToken);
//
//        HttpEntity<BitlyRequest> entity = new HttpEntity<>(request, headers);
//        BitlyResponse response = restTemplate.postForObject(url, entity, BitlyResponse.class);
//
//        return response != null ? response.getLink() : null;
//    }


}
