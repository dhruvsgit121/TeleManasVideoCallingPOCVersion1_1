package com.example.ehrc.telemanas.Utilities;

import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.User;
import com.example.ehrc.telemanas.Service.ParticipantService;
import com.example.ehrc.telemanas.Service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Service
public class VideoCallingUtilities {

    @Value("${jwt.jitsiFullDomain}")
    private String jitsiFullDomain;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private UserService userService;

//    public ResponseEntity<Map<String, Object>> getErrorMessageResponseEntity(String errorMessage, HttpStatusCode statusCode) {
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("errorMessage", errorMessage);
//        return new ResponseEntity<>(errorResponse, statusCode);
//    }

    public ResponseEntity<Map<String, Object>> getErrorResponseMessageEntity(String errorMessage, HttpStatusCode statusCode) {
        Map<String, Object> errorResponse = new HashMap<>();
        String responseErrorMessage =  (!errorMessage.equals(null)) ? errorMessage : "Some error occurred.";
//        HttpStatusCode responseErrorStatusCode =  statusCode. ? errorMessage : "Some error occured.";
        errorResponse.put(VideoCallingAPIConstants.errorMessageValue, responseErrorMessage);
        errorResponse.put(VideoCallingAPIConstants.isErrorFlagValue, true);
        return new ResponseEntity<>(errorResponse, statusCode);
    }



    public ResponseEntity<Map<String, Object>> getGlobalErrorResponseMessageEntity(String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        String responseErrorMessage =  (!errorMessage.equals(null)) ? errorMessage : "Some error occurred.";
        errorResponse.put(VideoCallingAPIConstants.errorMessageValue, responseErrorMessage);
        errorResponse.put(VideoCallingAPIConstants.isErrorFlagValue, true);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }



    public static long getExpirationTimeStamp(int timeStampOffset) {
        return Instant.now().plusSeconds(timeStampOffset).getEpochSecond();
    }

    public static long getCurrentTimeStamp() {
        return Instant.now().getEpochSecond();
    }

    public String generateRandomString(int stringLength) {
        boolean useLetters = true;
        boolean useNumbers = true;
        String generatedString = RandomStringUtils.random(stringLength, useLetters, useNumbers);
        return generatedString;
    }

    public LocalDateTime getDateTimeWithOffset(long offSet) {
        LocalDateTime now = LocalDateTime.now().plusSeconds(offSet);
        return now;
    }

    public String generateJWTURL(String roomID, String JWTToken) {
        return jitsiFullDomain + roomID + "?jwt=" + JWTToken;
    }

    public Participant getRequestedUserAsPerRequest(RoomDetailsRequestDTO roomDetailsRequest, ArrayList<Long> participantsList) {

        Participant firstParticipant = participantService.getParticipantByID(participantsList.get(0));
        Participant secondParticipant = participantService.getParticipantByID(participantsList.get(1));

        User firstUser = userService.getUserByID(firstParticipant.getParticipantId());

        if ((roomDetailsRequest.getIsMHP() == 1 && firstUser.getUserRole().equals(User.UserRole.MHP)) || (roomDetailsRequest.getIsMHP()  != 1 && firstUser.getUserRole().equals(User.UserRole.PATIENT))) {
            return firstParticipant;
        }
        return secondParticipant;
    }

}
