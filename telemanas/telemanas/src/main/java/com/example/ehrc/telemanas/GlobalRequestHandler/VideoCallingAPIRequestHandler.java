package com.example.ehrc.telemanas.GlobalRequestHandler;

import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class VideoCallingAPIRequestHandler {

    @Autowired
    private GlobalRequestHandler globalRequestHandler;

    public ResponseEntity<Map<String, Object>> makePostRequest(String apiURlPath, HttpEntity<String> requestEntity) {

        if (globalRequestHandler == null)
            globalRequestHandler = new GlobalRequestHandler();

        String requestURL = VideoCallingAPIConstants.eyAPIBaseURL + apiURlPath;

        // Make the POST request
        ResponseEntity<Map<String, Object>> responseEntity = globalRequestHandler.makePostRequest(requestURL, requestEntity);

        System.out.println("Response from parent is : " + responseEntity);
        // Return the response

        return responseEntity;
    }
}
