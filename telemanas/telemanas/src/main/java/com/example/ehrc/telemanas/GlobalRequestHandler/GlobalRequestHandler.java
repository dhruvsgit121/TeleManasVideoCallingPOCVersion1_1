package com.example.ehrc.telemanas.GlobalRequestHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GlobalRequestHandler {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<Map<String, Object>> makePostRequest(String url, HttpEntity<String> requestEntity) {

        if(restTemplate == null)
            restTemplate = new RestTemplate();
        // Make the POST request
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

        // Return the response
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
