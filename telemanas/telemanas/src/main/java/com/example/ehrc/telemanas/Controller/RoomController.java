package com.example.ehrc.telemanas.Controller;


import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
//import com.example.ehrc.telemanas.Model.EYDataModel.EYUserDataModal;
import com.example.ehrc.telemanas.Model.Participant;
//import com.example.ehrc.telemanas.Model.Room;
import com.example.ehrc.telemanas.Service.*;
import com.example.ehrc.telemanas.Service.NewServices.VideoCallService;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private AuthenticateUserFactory authenticateUserFactory;

//    @Value("${jwt.expirationOffSet}")
//    private int expirationOffset;
//
//    @Value("${sms.patientMessageURL}")
//    private String patientMessageURL;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private RoomService roomService;

//    @Autowired
//    private SMSService smsService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private VideoCallService videoCallService;

//    @Value("${jwt.RoomJWTValidityOffSet}")
//    private Long roomJWTValidityOffSet;

//    @Autowired
//    private JWTTokenService jwtTokenService;

    @Autowired
    private SSEService sseService;


//    public void sendLinkMesaageToPatient(String patientNumber, String roomShortCode) {
//        String messageText = patientMessageURL + roomShortCode;
//        String registeredMobileNumber = "+91" + patientNumber;
//        System.out.println("Message to send is : " + messageText);
//        System.out.println("Message send to number is : " + registeredMobileNumber);
//
//        //To be commented out to send SMS to the Patient...
//        //smsService.sendTestSms(registeredMobileNumber, messageText);
//    }


    @PostMapping("/getroomdetails")
    public ResponseEntity<Map<String, Object>> getVideoRoomDetails(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> roomServiceRequestData = roomService.generateVideoRoomDetailsResponseEntity(roomDetailsRequest);

        System.out.println("roomServiceRequestData" + roomServiceRequestData);

        if (roomServiceRequestData.containsKey("clientID")) {
            //User is a patient, who is joining the call...
            String clientID = roomServiceRequestData.get("clientID").toString();
            sseService.sendCustomMessage(clientID + "", "Hello patient joined the call...");
        }

        return new ResponseEntity(roomServiceRequestData, HttpStatus.OK);
    }


    @PostMapping("/joinroom")
    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();//new HashMap<>();

        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));

        System.out.println("Participant list is : " + participantsList);

//        return new ResponseEntity(responseData, HttpStatus.OK);

        if (participantsList.size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        //Setting the Join Date of the user...
        Participant requestedParticipant = videoCallingUtilities.getRequestedUserAsPerRequest(roomDetailsRequest, participantsList);
        requestedParticipant.setJoinDate(videoCallingUtilities.getDateTimeWithOffset(0));

        participantService.saveParticipant(requestedParticipant);

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    @PostMapping("/exitroom")
    public ResponseEntity<Map<String, Object>> setExitRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();

        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));

        if (participantsList.size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        //Setting the Left Date of the user...
        Participant requestedParticipant = videoCallingUtilities.getRequestedUserAsPerRequest(roomDetailsRequest, participantsList);
        requestedParticipant.setLeftDate(videoCallingUtilities.getDateTimeWithOffset(0));

        participantService.saveParticipant(requestedParticipant);

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    @RequestMapping("/createroom")
    public ResponseEntity<Map<String, Object>> createRoomWith(@Valid @RequestBody AuthenticateUserDTO userDTOData,
                                                              @RequestHeader("Authorization") String bearerToken,
                                                              @RequestHeader(value = "Loggedin") String loggedIn
    ) {

        String token = bearerToken.substring(7);

        //Adjusting The UserDTOData...
        userDTOData.setBearerToken(token);
        userDTOData.setLoggedInId(loggedIn);

        System.out.println("User data to be sent to api is : " + userDTOData);

        return videoCallService.createMeetingLink(userDTOData, authenticateUserFactory);

        //return roomService.createRequestedRoom(userDTOData, authenticateUserFactory, roomService, participantService);

//        EYUserDataModal userDataModal = null;
//        if (PatientResponseData.hasBody())
//            userDataModal = new EYUserDataModal(PatientResponseData.getBody());

//        ResponseEntity<Map<String, Object>> patientAuthenticationResponseData = roomService.autheticatePatientAndMHPData(userDTOData, authenticateUserFactory);
//
//        if (patientAuthenticationResponseData.getStatusCode() != HttpStatus.OK)
//            return patientAuthenticationResponseData;
//
//        //Parsing the Map data to EYUserDataModal Data Modal...
//        EYUserDataModal userDataModal = null;
//        if (patientAuthenticationResponseData.hasBody())
//            userDataModal = new EYUserDataModal(patientAuthenticationResponseData.getBody());

//        EYUserDataModal userDataModal = null;
//        if (patientAuthenticationResponseData.hasBody())
//            userDataModal = new EYUserDataModal(patientAuthenticationResponseData.getBody());


        //Checking for Authentication of Patient Data...
//        ResponseEntity<Map<String, Object>> PatientResponseData = authenticateUserFactory.authenticateUser("patient", userDTOData);
//        if (PatientResponseData.getStatusCode() != HttpStatus.OK)
//            return PatientResponseData;



        //Checking for Authentication of MHP...
//        ResponseEntity<Map<String, Object>> MHPResponseData = authenticateUserFactory.authenticateUser("mhp", userDTOData);
//        if (MHPResponseData.getStatusCode() != HttpStatus.OK)
//            return MHPResponseData;


//        //Decrypting Mobile Number of The Patient...
//        ResponseEntity<Map<String, Object>> decryptMobileData = authenticateUserFactory.decryptUserPhoneNumber(userDTOData, userDataModal.getEncryptedMobileNumber());
//        if (decryptMobileData.getStatusCode() != HttpStatus.OK)
//            return decryptMobileData;
//
//
//        System.out.println("Data Recieved is in mobile decrypt is : " + decryptMobileData.getBody());
//
//        if (decryptMobileData.hasBody()) {
//            Map<String, Object> encryptMobileNumberResponseData = decryptMobileData.getBody();
//            userDataModal.setMobileNumber((String) encryptMobileNumberResponseData.get("responsePhoneNo"));
//        }






//        ResponseEntity<Map<String, Object>> videoCallRoomData = roomService.createRoom(userDTOData, userDataModal, roomService, participantService, jwtTokenService, roomJWTValidityOffSet, expirationOffset);
//        if (videoCallRoomData.getStatusCode() != HttpStatus.OK)
//            return videoCallRoomData;
//
//        return videoCallRoomData;

//        System.out.println("Reponse data from video call generation is : " + videoCallRoomData.getBody());
//        System.out.println("Entered in existing room maps with room id " + videoCallRoomData.getBody());
//
////        System.out.println("Response after mobile decryption is : " + dencryptMobileData.hasBody());
//        System.out.println("User mobile number is : " + userDataModal.getMobileNumber());


    }

//    public ResponseEntity<Map<String, Object>> createRoom(AuthenticateUserDTO userDTOData, EYUserDataModal userDataModal) {
//
//        LocalDateTime expiryDate = videoCallingUtilities.getDateTimeWithOffset(roomJWTValidityOffSet);
//
//        ArrayList<String> roomShortCodesList = new ArrayList<>(participantService.getRoomShortCodeWith(userDTOData.getMhpUserName(), userDTOData.getTelemanasId(), expiryDate));
//
//        System.out.println("roomShortCodesList : " + roomShortCodesList);
//        System.out.println("roomShortCodesList : " + userDataModal.getMobileNumber());
//
//        //In case we have Already Room ID then we won't create it...
//        //Will return the already existing ROOM CODE...
//        if (roomShortCodesList.size() > 0) {
//            Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
//            String roomShortCode = roomShortCodesList.get(0);
//            responseMap.put("roomCode", roomShortCode);
//            System.out.println("Entered in existing room maps with room id " + roomShortCode);
//            sendLinkMesaageToPatient(userDataModal.getMobileNumber(), roomShortCode);
//            return new ResponseEntity(responseMap, HttpStatus.OK);
//        }
//
//        //In case we don't have existing Room ID...
//        //We will create and return the NEW ROOM CODE...
//        String roomID = videoCallingUtilities.generateRandomString(20);
//        String roomShortCode = videoCallingUtilities.generateRandomString(20);
//        String videoID = videoCallingUtilities.generateRandomString(20);
//
//        Room roomData = new Room(roomID, videoID, videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomShortCode);
//
//        String doctorJWTToken = jwtTokenService.generateJWTToken(userDTOData.getMhpUserName(), "dummyMHPemailid", roomID, true);
//        String patientJWTToken = jwtTokenService.generateJWTToken(userDTOData.getTelemanasId(), "dummyPatientemailid", roomID, false);
//
//        Participant mhpUser = new Participant(null, null, doctorJWTToken, userDTOData.getMhpUserName(), true, Participant.UserRole.MHP);
//        Participant patientUser = new Participant(null, null, patientJWTToken, userDTOData.getTelemanasId(), false, Participant.UserRole.PATIENT);
//
//        roomData.addParticipant(mhpUser);
//        roomData.addParticipant(patientUser);
//
//        Room savedRoomData = roomService.saveRoom(roomData);
//
//        Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
//        responseMap.put("roomCode", savedRoomData.getRoomShortCode());
//
//        sendLinkMesaageToPatient(userDataModal.getMobileNumber(), savedRoomData.getRoomShortCode());
//
//        return new ResponseEntity(responseMap, HttpStatus.OK);
//    }

}