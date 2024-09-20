package com.example.ehrc.telemanas.Service.NewServices;

import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.CustomException.RoomDoesNotExistException;
import com.example.ehrc.telemanas.CustomException.RoomNotActiveException;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.CallStartDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.DTO.VideoCallEventsDTO;
import com.example.ehrc.telemanas.Model.EYDataModel.MHPDataModal;
import com.example.ehrc.telemanas.Model.EYDataModel.PatientDataModal;
import com.example.ehrc.telemanas.Model.UpdatedModels.AuthenticatedUser;
//import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import com.example.ehrc.telemanas.Service.RoomService;
import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VideoCallService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TwilioSMSService twilioSMSService;

    @Autowired
    private UpdatedRoomService updatedRoomService;

    @Autowired
    private UpdatedParticipantService updatedParticipantService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private EventService eventService;


    //Method to Save Video Call Events...
    public ResponseEntity<Map<String, Object>> saveVideoCallEvents(VideoCallEventsDTO videoCallEventsDTO) {
        return eventService.saveEventData(videoCallEventsDTO.getRoomShortCode(), videoCallEventsDTO.getEventDescription());
    }

    //Method to Start Video Call...
    public ResponseEntity<Map<String, Object>> startVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        return roomService.startVideoCall(roomDetailsRequest);
    }

    //Method to get Patient Status whether he/she has joined room or not...
    public ResponseEntity<Map<String, Object>> getPatientJoinRoomStatusData(String roomShortCode) {
        return updatedRoomService.getPatientJoinData(roomShortCode);
    }

    //Method to Leave Video Call...
    public ResponseEntity<Map<String, Object>> leaveVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        return roomService.exitRoom(roomDetailsRequest);
    }

    //Method to Leave Video Call...
    public ResponseEntity<Map<String, Object>> leaveVideoCall(CallStartDTO callStartDTO) {

        ResponseEntity<Map<String, Object>> responseData = eventService.callEndSaveData(callStartDTO);
        if (responseData.getStatusCode() != HttpStatus.OK)
            return responseData;

        String eventDescription = (callStartDTO.getIsMHP() == 1) ? "MHP Ended the video call" : "Patient Ended the video call";
        ResponseEntity<Map<String, Object>> eventServiceResponseData = eventService.saveEventData(callStartDTO.getRoomShortCode(), eventDescription);

        boolean isErrorPresent = (boolean) (eventServiceResponseData.getBody().get("isErrorPresent"));

        if (isErrorPresent)
            return eventServiceResponseData;

        return roomService.exitRoom(callStartDTO);
    }


    public ResponseEntity<Map<String, Object>> JoinVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        return roomService.joinRoom(roomDetailsRequest);
    }

    //Method to Join Video Call...
    public ResponseEntity<Map<String, Object>> JoinVideoCall(CallStartDTO callStartDTO) {

        ResponseEntity<Map<String, Object>> responseData = eventService.callStartSaveData(callStartDTO);
        if (responseData.getStatusCode() != HttpStatus.OK)
            return responseData;

        String eventDescription = (callStartDTO.getIsMHP() == 1) ? "MHP Started the video call" : "Patient Started the video call";
        ResponseEntity<Map<String, Object>> eventServiceResponseData = eventService.saveEventData(callStartDTO.getRoomShortCode(), eventDescription);

        boolean isErrorPresent = (boolean) (eventServiceResponseData.getBody().get("isErrorPresent"));

        if (isErrorPresent)
            return eventServiceResponseData;

        return roomService.joinRoom(callStartDTO);
    }

    //Method to deactivated requested room...
    public ResponseEntity<Map<String, Object>> deactivateRequestedRoom(String roomShortCode) {
        return roomService.deactivateRequestedRoom(roomShortCode);
    }


    public ResponseEntity<Map<String, Object>> checkValidityOfSMSLinkForPatient(String roomShortCode) {
        return roomService.getValidityOfSMSLinkForPatient(roomShortCode);
    }


    public void saveIsActiveRoomOnJoinVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        roomService.saveIsActiveRoomOnJoinVideoCall(roomDetailsRequest);
    }

    public ResponseEntity<Map<String, Object>> createMeetingLink(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory) {

        System.out.println("Method called!!!");

        ResponseEntity<Map<String, Object>> patientAuthenticationResponseData = validatePatientData(userDTOData, authenticateUserFactory);//authenticationService.autheticateParticipantsData(userDTOData, authenticateUserFactory);

        if (patientAuthenticationResponseData.getStatusCode() != HttpStatus.OK)
            return patientAuthenticationResponseData;

        //Parsing the Map data to EYUserDataModal Data Modal...
        PatientDataModal patientUserDataModal = null;
        if (patientAuthenticationResponseData.hasBody())
            patientUserDataModal = new PatientDataModal(patientAuthenticationResponseData.getBody());


        ResponseEntity<Map<String, Object>> MHPAuthenticationResponseData = validateMHPData(userDTOData, authenticateUserFactory);//authenticationService.autheticateParticipantsData(userDTOData, authenticateUserFactory);

        if (MHPAuthenticationResponseData.getStatusCode() != HttpStatus.OK)
            return MHPAuthenticationResponseData;

        //Parsing the Map data to EYUserDataModal Data Modal...
        MHPDataModal mhpDataModal = null;
        if (MHPAuthenticationResponseData.hasBody())
            mhpDataModal = new MHPDataModal(MHPAuthenticationResponseData.getBody());

        System.out.println("USer MHP Data is : " + mhpDataModal);


        ResponseEntity<Map<String, Object>> patientMobileDecryptionResponseData = decryptPatientMobileNumber(authenticateUserFactory, userDTOData, patientUserDataModal);

        if (patientMobileDecryptionResponseData != null) {
            return patientMobileDecryptionResponseData;
        }

        ResponseEntity<Map<String, Object>> videoCallRoomData = roomService.createRoom(userDTOData, patientUserDataModal, mhpDataModal, roomService);
        sendMessageAfterParsingVideoCallRoomData(videoCallRoomData, patientUserDataModal);

        if (videoCallRoomData.hasBody() && videoCallRoomData.getBody().get("roomCode") != null) {
            String eventDescription = "MHP created the room.";
            ResponseEntity<Map<String, Object>> eventServiceResponseData = eventService.saveEventData(videoCallRoomData.getBody().get("roomCode").toString(), eventDescription);
            boolean isErrorPresent = (boolean) (eventServiceResponseData.getBody().get("isErrorPresent"));
            if (isErrorPresent)
                return eventServiceResponseData;
        }
        return videoCallRoomData;
    }

    private void sendMessageAfterParsingVideoCallRoomData(ResponseEntity<Map<String, Object>> videoCallRoomData, PatientDataModal userDataModal) {
        if (videoCallRoomData.hasBody()) {
            String roomShortCode = videoCallRoomData.getBody().get("roomCode").toString();
            sendSMSToPatient(userDataModal.getMobileNumber(), roomShortCode);
        }
    }


    public void sendSMSToPatient(String mobileNumber, String roomShortCode) {
        sendLinkToPatient(mobileNumber, roomShortCode);
    }

    private void sendLinkToPatient(String patientNumber, String roomShortCode) {
        String registeredMobileNumber = "+91" + patientNumber;
        //To be commented out to send SMS to the Patient...
        twilioSMSService.sendTestSms(registeredMobileNumber, roomShortCode);
    }


    public ResponseEntity<Map<String, Object>> decryptPatientMobileNumber(AuthenticateUserFactory authenticateUserFactory, AuthenticateUserDTO userDTOData, PatientDataModal userDataModal) {

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


    public ResponseEntity<Map<String, Object>> decryptPatientMobileNumberForResendSMS(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory, String roomShortCode) {

        Room roomData = updatedRoomService.findRoomDetailsWith(roomShortCode);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put(VideoCallingAPIConstants.isErrorFlagValue, false);

        String encryptedPhoneNumber = getEncryptedPhoneNumber(roomData);

        //Decrypting Mobile Number of The Patient...
        ResponseEntity<Map<String, Object>> decryptMobileData = authenticateUserFactory.decryptUserPhoneNumber(userDTOData, encryptedPhoneNumber);
        if (decryptMobileData.getStatusCode() != HttpStatus.OK)
            return decryptMobileData;

        System.out.println("Data Recieved is in mobile decrypt is : " + decryptMobileData.getBody());

        String errorMessage = "Some error Occurred. Please try again later.";

        if (decryptMobileData.hasBody()) {
            if (decryptMobileData.getBody().containsKey("responsePhoneNo")) {
                String mobileNumber = decryptMobileData.getBody().get("responsePhoneNo").toString();
                sendSMSToPatient(mobileNumber, roomShortCode);
                return new ResponseEntity<>(responseData, HttpStatus.OK);
            }
            if (decryptMobileData.getBody().containsKey("message") && decryptMobileData.getBody().get("message") != null) {
                errorMessage = decryptMobileData.getBody().get("message").toString();
            }
        }
        responseData.put(VideoCallingAPIConstants.errorMessageValue, errorMessage);
        return new ResponseEntity<>(responseData, HttpStatus.SEE_OTHER);
    }

    private static String getEncryptedPhoneNumber(Room roomData) {
        if (roomData == null || roomData.getParticipants().size() != 2)
            throw new RoomDoesNotExistException(null);

        if (!roomData.isActive())
            throw new RoomNotActiveException(null);

        AuthenticatedUser patientData = roomData.getParticipants().get(0).getAuthenticatedUser().getUserRole().equals(AuthenticatedUser.UserRole.PATIENT) ? roomData.getParticipants().get(0).getAuthenticatedUser() : roomData.getParticipants().get(1).getAuthenticatedUser();

        String encryptedPhoneNumber = patientData.getDecryptedMobileNumber();
        return encryptedPhoneNumber;
    }

    //Method to Validate Participants Patient...
    private ResponseEntity<Map<String, Object>> validatePatientData(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory) {
        return authenticationService.authenticatePatientData(userDTOData, authenticateUserFactory);
    }

    //Method to Validate Participants MHP...
    private ResponseEntity<Map<String, Object>> validateMHPData(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory) {
        return authenticationService.authenticateMHPData(userDTOData, authenticateUserFactory);
    }

}
