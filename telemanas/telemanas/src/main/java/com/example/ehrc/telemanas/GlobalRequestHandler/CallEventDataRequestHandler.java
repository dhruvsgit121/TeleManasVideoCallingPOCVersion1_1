package com.example.ehrc.telemanas.GlobalRequestHandler;


import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.CallStartDTO;
import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.aspectj.weaver.ast.Call;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CallEventDataRequestHandler {

    private final VideoCallingAPIRequestHandler videoCallingAPIRequestHandler = new VideoCallingAPIRequestHandler();
    private final VideoCallingUtilities videoCallingUtilities = new VideoCallingUtilities();



    public ResponseEntity<Map<String, Object>> saveCallStartData(CallStartDTO callStartDTO, String videoID) {

       String payload = "{\"eventId\":\"4\",\"attributes\":" +
               "[{\"key\":\"callType\",\"value\":\"inbound.manual.video\"}," +
               "{\"key\":\"ivrsCallId\",\"value\":\"" +
               callStartDTO.getIvrsCallID() +
               "\"}," +
               "{\"key\":\"stateId\",\"value\":\"35\"}," +
               "{\"key\":\"userUuid\",\"value\":\"" +
               callStartDTO.getUserUUID() +
               "\"}," +
               "{\"key\":\"referenceId\",\"value\":\"" +
               videoID +
               "\"}]}";

        System.out.println("payload is : " + payload);

        //Create HTTP Request Entity...
        HttpEntity<String> requestEntity = videoCallingUtilities.createHttpRequestEntity(callStartDTO.getLoggedInId(), callStartDTO.getBearerToken(), payload);
        //createHttpRequestEntity(userData, payload);//new HttpEntity<>(payload, headers);

        // Make the POST request
        ResponseEntity<Map<String, Object>> response = videoCallingAPIRequestHandler.makePostRequest(VideoCallingAPIConstants.callStartEventURL, requestEntity);

        Map<String, Object> parsedResponseData = null;

        System.out.println("response.getBody() in Encrypt Mobile Number is = " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            if(response.getBody().containsKey("code")){
                int responseCode = (int)response.getBody().get("code");
                return videoCallingUtilities.getErrorResponseMessageEntity(response.getBody().get("message").toString(), HttpStatusCode.valueOf(responseCode));
            }else{
                parsedResponseData = response.getBody();
                System.out.println("Entered in this with data being : " + parsedResponseData);
                return new ResponseEntity<>(parsedResponseData, HttpStatus.OK);
            }
        }
        return videoCallingUtilities.getGlobalErrorResponseMessageEntity(null);
    }



    public ResponseEntity<Map<String, Object>> saveCallEndData(CallStartDTO callStartDTO, String videoID, LocalDateTime callEndTime) {

        String payload = "{\"eventId\":\"5\",\"attributes\":" +
                "[{\"key\":\"callType\",\"value\":\"inbound.manual.video\"}," +
                "{\"key\":\"ivrsCallId\",\"value\":\"" +
                callStartDTO.getIvrsCallID() +
                "\"}," +
                "{\"key\":\"stateId\",\"value\":\"35\"}," +
                "{\"key\":\"userUuid\",\"value\":\"" +
                callStartDTO.getUserUUID() +
                "\"}," +
                "{\"key\":\"referenceId\",\"value\":\"" +
                videoID +
                "\"}," +
                "{\"key\":\"incompleteClosure\",\"value\":\"2\"}," +
                "{\"key\":\"callEndTime\",\"value\":\"" +
                callEndTime +
                "\"}]}";

        System.out.println("payload is : " + payload);

        //Create HTTP Request Entity...
        HttpEntity<String> requestEntity = videoCallingUtilities.createHttpRequestEntity(callStartDTO.getLoggedInId(), callStartDTO.getBearerToken(), payload);
        //createHttpRequestEntity(userData, payload);//new HttpEntity<>(payload, headers);

        // Make the POST request
        ResponseEntity<Map<String, Object>> response = videoCallingAPIRequestHandler.makePostRequest(VideoCallingAPIConstants.callStartEventURL, requestEntity);

        Map<String, Object> parsedResponseData = null;

        System.out.println("response.getBody() in call end is = " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            if(response.getBody().containsKey("code")){
                int responseCode = (int)response.getBody().get("code");
                return videoCallingUtilities.getErrorResponseMessageEntity(response.getBody().get("message").toString(), HttpStatusCode.valueOf(responseCode));
            }else{
                parsedResponseData = response.getBody();
                System.out.println("Entered in this with data being : " + parsedResponseData);
                return new ResponseEntity<>(parsedResponseData, HttpStatus.OK);
            }
        }
        return videoCallingUtilities.getGlobalErrorResponseMessageEntity(null);
    }

}
