package com.example.ehrc.telemanas.Service;

import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Model.EYDataModel.EYUserDataModal;
import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.Room;
import com.example.ehrc.telemanas.UserRepository.RoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    private final SMSService smsService;

//    @Autowired
//    private JWTTokenService jwtTokenService;

//    @Autowired
    private final JWTTokenService jwtTokenService;

    @Autowired
    public RoomService(SMSService smsService, JWTTokenService jwtTokenService) {
        this.smsService = smsService;
        this.jwtTokenService = jwtTokenService;
    }


    @Value("${jwt.RoomJWTValidityOffSet}")
    private Long roomJWTValidityOffSet;

    @Value("${jwt.expirationOffSet}")
    private int expirationOffset;

    @Value("${sms.patientMessageURL}")
    private String patientMessageURL;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room getRoomDetailsWith(String shortCode) {
        return roomRepository.findRoomDetailsWith(shortCode);
    }

    public List<Room> getRoomListWithExpirationdate(LocalDateTime expirationDate) {
        return roomRepository.findRoomListWithExpirationDate(expirationDate);
    }


    //####################################### Methods for Room Creation #####################################


    public ResponseEntity<Map<String, Object>> createRequestedRoom(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory, RoomService roomService, ParticipantService participantService) {

        ResponseEntity<Map<String, Object>> patientAuthenticationResponseData = autheticatePatientAndMHPData(userDTOData, authenticateUserFactory);

        System.out.println("Method for checking the createRequestedRoom : " + this.smsService);
        System.out.println("value of myTestRoomJWTValidityOffSet : " + roomJWTValidityOffSet);

        if (patientAuthenticationResponseData.getStatusCode() != HttpStatus.OK)
            return patientAuthenticationResponseData;

        //Parsing the Map data to EYUserDataModal Data Modal...
        EYUserDataModal userDataModal = null;
        if (patientAuthenticationResponseData.hasBody())
            userDataModal = new EYUserDataModal(patientAuthenticationResponseData.getBody());

        ResponseEntity<Map<String, Object>> patientMobileDecryptionResponseData = decryptPatientMobileNumber(authenticateUserFactory, userDTOData, userDataModal);

        if (patientMobileDecryptionResponseData != null) {
            return patientMobileDecryptionResponseData;
        }

        System.out.println("Mobile decrypted number is : " + userDataModal.getEncryptedMobileNumber());

        ResponseEntity<Map<String, Object>> videoCallRoomData = roomService.createRoom(userDTOData, userDataModal, roomService, participantService, jwtTokenService);

        return videoCallRoomData;
    }


    public ResponseEntity<Map<String, Object>> decryptPatientMobileNumber(AuthenticateUserFactory authenticateUserFactory, AuthenticateUserDTO userDTOData, EYUserDataModal userDataModal) {

        //Decrypting Mobile Number of The Patient...
        ResponseEntity<Map<String, Object>> decryptMobileData = authenticateUserFactory.decryptUserPhoneNumber(userDTOData, userDataModal.getEncryptedMobileNumber());
        if (decryptMobileData.getStatusCode() != HttpStatus.OK)
            return decryptMobileData;

        System.out.println("Data Recieved is in mobile decrypt is : " + decryptMobileData.getBody());

        if (decryptMobileData.hasBody()) {
            Map<String, Object> encryptMobileNumberResponseData = decryptMobileData.getBody();
            userDataModal.setMobileNumber((String) encryptMobileNumberResponseData.get("responsePhoneNo"));
        }

        return null;
    }


    public void sendLinkSMSToPatient(SMSService smsService, String patientMessageURL, String patientNumber, String roomShortCode) {
        String messageText = patientMessageURL + roomShortCode;
        String registeredMobileNumber = "+91" + "9015346166";
//        String registeredMobileNumber = "+91" + patientNumber;
        System.out.println("Message to send is : " + messageText);
        System.out.println("Message send to number is : " + registeredMobileNumber);

        //To be commented out to send SMS to the Patient...
        //smsService.sendTestSms(registeredMobileNumber, messageText);
    }


    public ResponseEntity<Map<String, Object>> autheticatePatientAndMHPData(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory) {

        //Checking for Authentication of Patient Data...
        ResponseEntity<Map<String, Object>> PatientResponseData = authenticateUserFactory.authenticateUser("patient", userDTOData);
        if (PatientResponseData.getStatusCode() != HttpStatus.OK)
            return PatientResponseData;

        //Checking for Authentication of MHP...
        ResponseEntity<Map<String, Object>> MHPResponseData = authenticateUserFactory.authenticateUser("mhp", userDTOData);
        if (MHPResponseData.getStatusCode() != HttpStatus.OK)
            return MHPResponseData;

        return PatientResponseData;
    }


    public ResponseEntity<Map<String, Object>> processAlreadyExistedRoom(ArrayList<String> roomShortCodesList, SMSService smsService, EYUserDataModal userDataModal, String patientMessageURL) {

        //In case we have Already Room ID then we won't create it...
        //Will return the already existing ROOM CODE...
        if (roomShortCodesList.size() > 0) {
            Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
            String roomShortCode = roomShortCodesList.get(0);
            responseMap.put("roomCode", roomShortCode);
            System.out.println("Entered in existing room maps with room id " + roomShortCode);
            sendLinkSMSToPatient(smsService, patientMessageURL, userDataModal.getMobileNumber(), roomShortCode);
            return new ResponseEntity(responseMap, HttpStatus.OK);
        }
        return null;
    }


    public ResponseEntity<Map<String, Object>> createRoom(AuthenticateUserDTO userDTOData, EYUserDataModal userDataModal, RoomService roomService, ParticipantService participantService, JWTTokenService jwtTokenService) {

        LocalDateTime expiryDate = videoCallingUtilities.getDateTimeWithOffset(roomJWTValidityOffSet);

        ArrayList<String> roomShortCodesList = new ArrayList<>(participantService.getRoomShortCodeWith(userDTOData.getMhpUserName(), userDTOData.getTelemanasId(), expiryDate));

        System.out.println("roomShortCodesList : " + roomShortCodesList);
        System.out.println("roomShortCodesList : " + userDataModal.getMobileNumber());

        //In case we have Already Room ID then we won't create it...
        //Will return the already existing ROOM CODE...
//        if (roomShortCodesList.size() > 0) {
//            Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
//            String roomShortCode = roomShortCodesList.get(0);
//            responseMap.put("roomCode", roomShortCode);
//            System.out.println("Entered in existing room maps with room id " + roomShortCode);
//            sendLinkSMSToPatient(smsService, patientMessageURL, userDataModal.getMobileNumber(), roomShortCode);
//            return new ResponseEntity(responseMap, HttpStatus.OK);
//        }

        ResponseEntity<Map<String, Object>> alreadyExistedRoomResponseData = processAlreadyExistedRoom(roomShortCodesList, smsService, userDataModal, patientMessageURL);
        if (alreadyExistedRoomResponseData != null)
            return alreadyExistedRoomResponseData;

        //In case we don't have existing Room ID...
        //We will create and return the NEW ROOM CODE...
        String roomID = videoCallingUtilities.generateRandomString(20);
        String roomShortCode = videoCallingUtilities.generateRandomString(20);
        String videoID = videoCallingUtilities.generateRandomString(20);

        return processNewlyCreatedRoom(userDTOData, userDataModal, roomService, jwtTokenService, smsService, expirationOffset, patientMessageURL, roomShortCode, roomID, videoID);

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
//        sendLinkSMSToPatient(smsService, patientMessageURL, userDataModal.getMobileNumber(), roomShortCode);
//        return new ResponseEntity(responseMap, HttpStatus.OK);
    }


    public ResponseEntity<Map<String, Object>> processNewlyCreatedRoom(AuthenticateUserDTO userDTOData, EYUserDataModal userDataModal, RoomService roomService, JWTTokenService jwtTokenService, SMSService smsService, long expirationOffset, String patientMessageURL, String roomShortCode, String roomID, String videoID) {

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

        sendLinkSMSToPatient(smsService, patientMessageURL, userDataModal.getMobileNumber(), roomShortCode);
        return new ResponseEntity(responseMap, HttpStatus.OK);
    }


    public Map<String, Object> generateVideoRoomDetailsResponseEntity(RoomDetailsRequestDTO roomDetailsRequest) {

        Room roomDetails = this.getRoomDetailsWith(roomDetailsRequest.getRoomShortCode());

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

        processParticipantsJWTTokenData(roomDetailsRequest, roomDetails, firstParticipant, secondParticipant, responseData);

        return responseData;
    }


    public void processParticipantsJWTTokenData(RoomDetailsRequestDTO roomDetailsRequest, Room roomDetails, Participant firstParticipant, Participant secondParticipant, Map<String, Object> responseData) {

        if ((roomDetailsRequest.getIsMHP() == 1 && firstParticipant.getUserRole().equals(Participant.UserRole.MHP)) || (roomDetailsRequest.getIsMHP() != 1 && firstParticipant.getUserRole().equals(Participant.UserRole.PATIENT))) {
            responseData.put("jwtToken", firstParticipant.getJwtToken());
            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), firstParticipant.getJwtToken()));
        } else {
            responseData.put("jwtToken", secondParticipant.getJwtToken());
            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), secondParticipant.getJwtToken()));
        }

        if (roomDetailsRequest.getIsMHP() != 1) {
            String clientID = firstParticipant.getUserRole().equals(Participant.UserRole.MHP) ? firstParticipant.getParticipantId() : secondParticipant.getParticipantId();
            responseData.put("clientID", clientID);
        }
    }
}
