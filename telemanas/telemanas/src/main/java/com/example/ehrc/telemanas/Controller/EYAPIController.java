package com.example.ehrc.telemanas.Controller;

import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestHeader;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/eyi")
@Validated

public class EYAPIController {


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

    @Autowired
    private SSEService sseService;

    @Autowired
    private SMSService smsService;


    @Autowired
    private AuthenticateUserFactory authenticateUserFactory;

    @RequestMapping("/gethelloworld")
    public ResponseEntity<Map<String, Object>> getHelloWorld(@Valid @RequestBody AuthenticateUserDTO userDTOData,
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

        EYUserDataModal userDataModal = null;
        if (PatientResponseData.hasBody())
            userDataModal = new EYUserDataModal(PatientResponseData.getBody());

        //Checking for Authentication of MHP...
        ResponseEntity<Map<String, Object>> MHPResponseData = authenticateUserFactory.authenticateUser("mhp", userDTOData);
        if (MHPResponseData.getStatusCode() != HttpStatus.OK)
            return MHPResponseData;

        ResponseEntity<Map<String, Object>> videoCallRoomData = createRoom(userDTOData);
        if (videoCallRoomData.getStatusCode() != HttpStatus.OK)
            return videoCallRoomData;

        System.out.println("Reposne data from video call generation is : " + videoCallRoomData.getBody());
        System.out.println("Entered in existing room maps with room id " + videoCallRoomData.getBody());

        //Decrypting Mobile Number of The Patient...
        //ResponseEntity<Map<String, Object>> dencryptMobileData = authenticateUserFactory.decryptUserPhoneNumber(userDTOData, userDataModal.getMobileNumber());
//        if (MHPResponseData.getStatusCode() != HttpStatus.OK)
//            return response;

        //System.out.println("Response after mobile decryption is : " + dencryptMobileData.hasBody());

//        System.out.println("Encrypted mobile number is : " + userDataModal.getEncryptedMobileNumber());

        return videoCallRoomData;
    }

    public ResponseEntity<Map<String, Object>> createRoom(AuthenticateUserDTO userDTOData){

        LocalDateTime expiryDate = videoCallingUtilities.getDateTimeWithOffset(roomJWTValidityOffSet);

        //System.out.println("Exppiration time is : " + expiryDate);
        //System.out.println("Room code list is : " + participantService.getRoomShortCodeWith(createRoomData.getMhpID(), createRoomData.getPatientId(), expiryDate));

        //System.out.println("Room code list is for telemanas id  : " +  userDTOData.getTelemanasId() + " getMhpUserName : " + userDTOData.getMhpUserName() + " expiryDate : " + expiryDate);

       //userDTOData
        ArrayList<String> roomShortCodesList = new ArrayList<>(participantService.getRoomShortCodeWith(userDTOData.getMhpUserName(), userDTOData.getTelemanasId(), expiryDate));

        //System.out.println("MHP user name is : " + extractedMPHUser.getUserName());
        System.out.println("roomShortCodesList : " + roomShortCodesList);

        //In case we have Already Room ID then we won't create it...
        //Will return the already existing ROOM CODE...
        if (roomShortCodesList.size() > 0) {
            Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
            String roomShortCode = roomShortCodesList.get(0);
            responseMap.put("roomCode", roomShortCode);
            System.out.println("Entered in existing room maps with room id " + roomShortCode);
            //sendLinkMesaageToPatient(extractedPatientUser.getContactNumber(), roomShortCode);
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
//
        Participant mhpUser = new Participant(null, null, doctorJWTToken, userDTOData.getMhpUserName());
        Participant patientUser = new Participant(null, null, patientJWTToken, userDTOData.getTelemanasId());

        roomData.addParticipant(mhpUser);
        roomData.addParticipant(patientUser);

        Room savedRoomData = roomService.saveRoom(roomData);

        Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
        responseMap.put("roomCode", savedRoomData.getRoomShortCode());

//        sendLinkMesaageToPatient(extractedPatientUser.getContactNumber(), savedRoomData.getRoomShortCode());

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }
}
