package com.example.ehrc.telemanas.GlobalRequestHandler.AutheticateUserRequestHandler;


import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.GlobalRequestHandler.VideoCallingAPIRequestHandler;
import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class AuthenticateUserRequestHandler {

    @Autowired
    private VideoCallingAPIRequestHandler videoCallingAPIRequestHandler;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    public ResponseEntity<Map<String, Object>> autheticateUserData(AuthenticateUserDTO userData) {

        if (videoCallingAPIRequestHandler == null)
            videoCallingAPIRequestHandler = new VideoCallingAPIRequestHandler();

        if (videoCallingUtilities == null)
            videoCallingUtilities = new VideoCallingUtilities();

        String payload = "{\"telemanasId\":\"" +
                userData.getTelemanasId() +
                "\",\"userUuid\":\"" +
                userData.getUserUuid() +
                "\"}";

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("loggedin", userData.getLoggedInId());
        // Optionally set authorization or other headers
        headers.set("Authorization", "Bearer " + userData.getBearerToken());
        // Create the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        // Make the POST request
        ResponseEntity<Map<String, Object>> response = videoCallingAPIRequestHandler.makePostRequest(VideoCallingAPIConstants.authenticatePatientURL, requestEntity);

        Map<String, Object> parsedResponseData = null;

        System.out.println("response.getBody() = " + response.getBody());

        if (response.getBody().containsKey("code")) {
            int responseCode = (int) response.getBody().get("code");
            if (responseCode == 200) {
                if (response.getBody().containsKey("payload")) {
                    ArrayList userResponseData = (ArrayList) response.getBody().get("payload");
                    if (userResponseData.size() > 0) {
                        Map<String, Object> userDataToBeParsed = (Map<String, Object>) userResponseData.get(0);
                        parsedResponseData = userDataToBeParsed;
                    }
                }
                return new ResponseEntity<>(parsedResponseData, HttpStatus.OK);
            } else {
                return videoCallingUtilities.getErrorResponseMessageEntity(response.getBody().get("message").toString(), HttpStatusCode.valueOf(responseCode));
            }
        }
        return videoCallingUtilities.getErrorResponseMessageEntity(null, HttpStatus.FORBIDDEN);
    }
}
