package com.example.ehrc.telemanas.GlobalRequestHandler.AutheticateUserRequestHandler;


import com.example.ehrc.telemanas.GlobalRequestHandler.VideoCallingAPIRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthenticateUserRequestHandler {

    @Autowired
    private VideoCallingAPIRequestHandler videoCallingAPIRequestHandler;

//    public ResponseEntity<Map<String, Object>> autheticateUserData() {

        public void autheticateUserData() {

        if(videoCallingAPIRequestHandler == null)
            videoCallingAPIRequestHandler = new VideoCallingAPIRequestHandler();





//        // Make the POST request
//        ResponseEntity<Map> response = new ResponseEntity<>();

        // restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
//
//        // Return the response
//        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

}
