package com.example.ehrc.telemanas.Utilities;

import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
//import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationRoom;
import com.example.ehrc.telemanas.Model.UpdatedModels.AuthenticatedUser;
import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
//import com.example.ehrc.telemanas.Service.ParticipantService;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationRoomRepository;
import org.apache.commons.lang3.RandomStringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Instant;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Service
public class VideoCallingUtilities {

    @Value("${jwt.jitsiFullDomain}")
    private String jitsiFullDomain;

    public String APIRequest_Header_LoggedIn_Key="";

    @Autowired
    private VideoConsultationRoomRepository videoConsultationRoomRepository;


//    @Autowired
//    private ParticipantService participantService;

//    @Autowired
//    private UserService userService;

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


    public Map<String, Object> getSuccessResponseMap() {
        Map<String, Object> successResponseMap = new HashMap<>();
        successResponseMap.put(VideoCallingAPIConstants.isErrorFlagValue, false);
        return successResponseMap;
    }


    public ResponseEntity<Map<String, Object>> getRoomActivationCheckResponseMap(String roomShortCode) {
        VideoConsultationRoom room = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(roomShortCode);
        if (room == null)
            return getErrorResponseMessageEntity(VideoCallingAPIConstants.ERROR_MESSAGE_ROOM_DOES_NOT_EXISTS, HttpStatus.SEE_OTHER);
        return null;
    }

//    VideoConsultationRoom room = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(callStartDTO.getRoomShortCode());
//
//        if (room == null)
//            return roomService.getRoomValidationResponseEntity();



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

//    public Participant getRequestedUserAsPerRequest(RoomDetailsRequestDTO roomDetailsRequest, ArrayList<Long> participantsList) {
//
//        Participant firstParticipant = participantService.getParticipantByID(participantsList.get(0));
//        Participant secondParticipant = participantService.getParticipantByID(participantsList.get(1));
//
//        if((firstParticipant.getUserRole().equals(Participant.UserRole.MHP) && roomDetailsRequest.getIsMHP() == 1) ||
//                (firstParticipant.getUserRole().equals(Participant.UserRole.PATIENT) && roomDetailsRequest.getIsMHP() == 0) ){
//            return firstParticipant;
//        }
//
//        return secondParticipant;
//    }


    public Participant getRequestedUpdatedUserAsPerRequest(Room roomData, RoomDetailsRequestDTO roomDetailsRequest) {

        Participant firstParticipant = roomData.getParticipants().get(0);
        Participant secondParticipant = roomData.getParticipants().get(1);

        if((firstParticipant.getAuthenticatedUser().getUserRole().equals(AuthenticatedUser.UserRole.MHP) && roomDetailsRequest.getIsMHP() == 1) ||
                (firstParticipant.getAuthenticatedUser().getUserRole().equals(AuthenticatedUser.UserRole.PATIENT) && roomDetailsRequest.getIsMHP() == 0) ){
            return firstParticipant;
        }
        return secondParticipant;
    }


    public HttpEntity<String> createHttpRequestEntity(String loggedInID, String bearerToken, String payload) {

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("loggedin", loggedInID);
        // Optionally set authorization or other headers
        headers.set("Authorization", "Bearer " + bearerToken);
        // Create the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        return requestEntity;
    }

}
