package com.example.ehrc.telemanas.Controller;


import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.CreateRoomDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Model.EYDataModel.EYUserDataModal;
import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.Room;
//import com.example.ehrc.telemanas.Model.User;
import com.example.ehrc.telemanas.Service.*;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private AuthenticateUserFactory authenticateUserFactory;

    @Value("${jwt.expirationOffSet}")
    private int expirationOffset;

    @Value("${sms.patientMessageURL}")
    private String patientMessageURL;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private RoomService roomService;

//    @Autowired
//    private UserService userService;

    @Autowired
    private ParticipantService participantService;

    @Value("${jwt.RoomJWTValidityOffSet}")
    private Long roomJWTValidityOffSet;

    @Autowired
    private JWTTokenService jwtTokenService;
    //
    @Autowired
    private SSEService sseService;
//
    @Autowired
    private SMSService smsService;


//    @PostMapping("/createroom")
//    public ResponseEntity<Room> createVideoCallingRoomDummy(@Valid @RequestBody CreateRoomDTO createRoomData) {

        /*if (createRoomData.getPatientId() == null) {
            throw new ValidationMessagesException("Patient ID cannot be null.");
        } else if (createRoomData.getMhpID() == null) {
            throw new ValidationMessagesException("MHP ID cannot be null.");
        }

        Optional<User> MHPUser = userService.getUserByID(createRoomData.getMhpID(), User.UserRole.MHP.toString());//Optional.ofNullable(userService.getUserByID(createRoomData.getMhpID()));
        User extractedMPHUser, extractedPatientUser;

        Optional<User> PatientUser = userService.getUserByID(createRoomData.getPatientId(), User.UserRole.PATIENT.toString());//Optional.ofNullable(userService.getUserByID(createRoomData.getMhpID()));

        if (MHPUser.isPresent()) {
            extractedMPHUser = MHPUser.get();
        } else {
            throw new ValidationMessagesException("MHP ID with this role does not exist.");
        }

        if (PatientUser.isPresent()) {
            extractedPatientUser = PatientUser.get();
        } else {
            throw new ValidationMessagesException("Patient ID with this role does not exist.");
        }

        LocalDateTime expiryDate = videoCallingUtilities.getDateTimeWithOffset(roomJWTValidityOffSet);

        System.out.println("Exppiration time is : " + expiryDate);
        System.out.println("Room code list is : " + participantService.getRoomShortCodeWith(createRoomData.getMhpID(), createRoomData.getPatientId(), expiryDate));

        ArrayList<String> roomShortCodesList = new ArrayList<>(participantService.getRoomShortCodeWith(createRoomData.getMhpID(), createRoomData.getPatientId(), expiryDate));

        System.out.println("MHP user name is : " + extractedMPHUser.getUserName());
        System.out.println("roomShortCodesList : " + roomShortCodesList);

        //In case we have Already Room ID then we won't create it...
        //Will return the already existing ROOM CODE...
        if (roomShortCodesList.size() > 0) {
            Map<String, Object> responseMap = new HashMap<>();
            String roomShortCode = roomShortCodesList.get(0);
            responseMap.put("roomCode", roomShortCode);
            sendLinkMesaageToPatient(extractedPatientUser.getContactNumber(), roomShortCode);
            System.out.println("roomShortCodesList : " + roomShortCodesList);
            return new ResponseEntity(responseMap, HttpStatus.OK);
        }

        //In case we don't have existing Room ID...
        //We will create and return the NEW ROOM CODE...
        String roomID = videoCallingUtilities.generateRandomString(20);
        String roomShortCode = videoCallingUtilities.generateRandomString(20);

        Room roomData = new Room(roomID, videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomShortCode);

        String doctorJWTToken = jwtTokenService.generateJWTToken(extractedMPHUser.getUserName(), extractedMPHUser.getUserEmail(), roomID, true);
        String patientJWTToken = jwtTokenService.generateJWTToken(extractedPatientUser.getUserName(), extractedPatientUser.getUserEmail(), roomID, false);

        //Participant mhpUser = new Participant(null, null, doctorJWTToken, createRoomData.getMhpID());
        //Participant patientUser = new Participant(null, null, patientJWTToken, createRoomData.getPatientId());

//        roomData.addParticipant(mhpUser);
//        roomData.addParticipant(patientUser);
//
//        Room savedRoomData = roomService.saveRoom(roomData);
//
//        Map<String, Object> responseMap = new HashMap<>();
//        responseMap.put("roomCode", savedRoomData.getRoomShortCode());
//
//        sendLinkMesaageToPatient(extractedPatientUser.getContactNumber(), savedRoomData.getRoomShortCode());
//

*/

//        Map<String, Object> responseMap = new HashMap<>();
//        return new ResponseEntity(responseMap, HttpStatus.OK);
//    }


    public void sendLinkMesaageToPatient(String patientNumber, String roomShortCode) {
        String messageText = patientMessageURL + roomShortCode;
        String registeredMobileNumber = "+91" + patientNumber;
        System.out.println("Message to send is : " + messageText);

        //To be commented out to send SMS to the Patient...
        //smsService.sendTestSms(registeredMobileNumber, messageText);
    }


    @PostMapping("/getroomdetails")
    public ResponseEntity<Map<String, Object>> getVideoRoomDetails(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        Room roomDetails = roomService.getRoomDetailsWith(roomDetailsRequest.getRoomShortCode());

        if (roomDetails == null || !roomDetails.isActive() || roomDetails.getParticipants().size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        ArrayList<Participant> participantsList = new ArrayList<>(roomDetails.getParticipants());

        Map<String, Object> responseData = new HashMap<>();

        Participant firstParticipant = participantsList.get(0);
        Participant secondParticipant = participantsList.get(1);

        System.out.println("first participant data is :" + firstParticipant.getParticipantId());
        System.out.println("Second participant data is :" + secondParticipant.getParticipantId());

        responseData.put("roomID", roomDetails.getRoomId());

        if ((roomDetailsRequest.getIsMHP() == 1 && firstParticipant.getUserRole().equals(Participant.UserRole.MHP)) || (roomDetailsRequest.getIsMHP() != 1 && firstParticipant.getUserRole().equals(Participant.UserRole.PATIENT))) {
            responseData.put("jwtToken", firstParticipant.getJwtToken());
            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), firstParticipant.getJwtToken()));
        } else {
            responseData.put("jwtToken", secondParticipant.getJwtToken());
            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), secondParticipant.getJwtToken()));
        }
//
        if (roomDetailsRequest.getIsMHP() != 1) {
            System.out.println("entered in the loop!!!");
            //User is a patient, who is joining the call...
            String clientID = firstParticipant.getUserRole().equals(Participant.UserRole.MHP) ? firstParticipant.getParticipantId() : secondParticipant.getParticipantId();
            sseService.sendCustomMessage(clientID + "", "Hello patient joined the call...");
        }
//
        System.out.println("Response data from the /getroomdetails API is : " + responseData);

        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    @PostMapping("/joinroom")
    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> responseData = new HashMap<>();

        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));

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

        Map<String, Object> responseData = new HashMap<>();

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
                                                             @RequestHeader(value = "BearerToken") String bearerToken,
                                                             @RequestHeader(value = "loggedin") String loggedIn
    ) {

        userDTOData.setBearerToken(bearerToken);
        userDTOData.setLoggedInId(loggedIn);

        System.out.println("User data to be sent to api is : " + userDTOData);

        //Checking for Authentication of Patient Data...
        ResponseEntity<Map<String, Object>> PatientResponseData = authenticateUserFactory.authenticateUser("patient", userDTOData);
        if (PatientResponseData.getStatusCode() != HttpStatus.OK)
            return PatientResponseData;

        //Parsing the Map data to EYUserDataModal Data Modal...
        EYUserDataModal userDataModal = null;
        if (PatientResponseData.hasBody())
            userDataModal = new EYUserDataModal(PatientResponseData.getBody());

        //Checking for Authentication of MHP...
        ResponseEntity<Map<String, Object>> MHPResponseData = authenticateUserFactory.authenticateUser("mhp", userDTOData);
        if (MHPResponseData.getStatusCode() != HttpStatus.OK)
            return MHPResponseData;



        //Decrypting Mobile Number of The Patient...
        ResponseEntity<Map<String, Object>> decryptMobileData = authenticateUserFactory.decryptUserPhoneNumber(userDTOData, userDataModal.getEncryptedMobileNumber());
        if (decryptMobileData.getStatusCode() != HttpStatus.OK)
            return decryptMobileData;

        System.out.println("Data Recieved is in mobile decrypt is : " + decryptMobileData.getBody());

        if(decryptMobileData.hasBody()){
            Map<String, Object> encryptMobileNumberResponseData = decryptMobileData.getBody();
            userDataModal.setMobileNumber((String) encryptMobileNumberResponseData.get("responsePhoneNo"));
        }



        ResponseEntity<Map<String, Object>> videoCallRoomData = createRoom(userDTOData, userDataModal);
        if (videoCallRoomData.getStatusCode() != HttpStatus.OK)
            return videoCallRoomData;

        System.out.println("Reponse data from video call generation is : " + videoCallRoomData.getBody());
        System.out.println("Entered in existing room maps with room id " + videoCallRoomData.getBody());

//        System.out.println("Response after mobile decryption is : " + dencryptMobileData.hasBody());
        System.out.println("User mobile number is : " + userDataModal.getMobileNumber());

        return videoCallRoomData;
    }

    public ResponseEntity<Map<String, Object>> createRoom(AuthenticateUserDTO userDTOData, EYUserDataModal userDataModal ) {

        LocalDateTime expiryDate = videoCallingUtilities.getDateTimeWithOffset(roomJWTValidityOffSet);

        ArrayList<String> roomShortCodesList = new ArrayList<>(participantService.getRoomShortCodeWith(userDTOData.getMhpUserName(), userDTOData.getTelemanasId(), expiryDate));

        System.out.println("roomShortCodesList : " + roomShortCodesList);

        System.out.println("roomShortCodesList : " + userDataModal.getMobileNumber());

        //In case we have Already Room ID then we won't create it...
        //Will return the already existing ROOM CODE...
        if (roomShortCodesList.size() > 0) {
            Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
            String roomShortCode = roomShortCodesList.get(0);
            responseMap.put("roomCode", roomShortCode);
            System.out.println("Entered in existing room maps with room id " + roomShortCode);
            sendLinkMesaageToPatient(userDataModal.getMobileNumber(), roomShortCode);
            return new ResponseEntity(responseMap, HttpStatus.OK);
        }

        //In case we don't have existing Room ID...
        //We will create and return the NEW ROOM CODE...
        String roomID = videoCallingUtilities.generateRandomString(20);
        String roomShortCode = videoCallingUtilities.generateRandomString(20);
        String videoID = videoCallingUtilities.generateRandomString(20);

        Room roomData = new Room(roomID, videoID, videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomShortCode);

        String doctorJWTToken = jwtTokenService.generateJWTToken(userDTOData.getMhpUserName(), "dummyMHPemailid", roomID, true);
        String patientJWTToken = jwtTokenService.generateJWTToken(userDTOData.getTelemanasId(), "dummyPatientemailid", roomID, false);

        Participant mhpUser = new Participant(null, null, doctorJWTToken, userDTOData.getMhpUserName(), true, Participant.UserRole.MHP);
        Participant patientUser = new Participant(null, null, patientJWTToken, userDTOData.getTelemanasId(), false, Participant.UserRole.PATIENT);

        roomData.addParticipant(mhpUser);
        roomData.addParticipant(patientUser);

        Room savedRoomData = roomService.saveRoom(roomData);

        Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
        responseMap.put("roomCode", savedRoomData.getRoomShortCode());

        sendLinkMesaageToPatient(userDataModal.getMobileNumber(), savedRoomData.getRoomShortCode());

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }


}