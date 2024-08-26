package com.example.ehrc.telemanas.Service.NewServices;

import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Model.EYDataModel.EYUserDataModal;
import com.example.ehrc.telemanas.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VideoCallService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RoomService roomService;


    public ResponseEntity<Map<String, Object>> startVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        return roomService.startVideoCall(roomDetailsRequest);
    }

    public ResponseEntity<Map<String, Object>> leaveVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        return roomService.exitRoom(roomDetailsRequest);
    }

    public ResponseEntity<Map<String, Object>> JoinVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        return roomService.joinRoom(roomDetailsRequest);
    }

    public ResponseEntity<Map<String, Object>> createMeetingLink(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory) {

        System.out.println("Method called!!!");

        ResponseEntity<Map<String, Object>> patientAuthenticationResponseData = validateParticipants(userDTOData, authenticateUserFactory);//authenticationService.autheticateParticipantsData(userDTOData, authenticateUserFactory);

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

        ResponseEntity<Map<String, Object>> videoCallRoomData = roomService.createRoom(userDTOData, userDataModal, roomService);
        sendMessageAfterParsingVideoCallRoomData(videoCallRoomData, userDataModal);
        return videoCallRoomData;
    }

    private void sendMessageAfterParsingVideoCallRoomData(ResponseEntity<Map<String, Object>> videoCallRoomData, EYUserDataModal userDataModal) {
        if (videoCallRoomData.hasBody()) {
            String roomShortCode = videoCallRoomData.getBody().get("roomCode").toString();
            sendLinkToPatient(userDataModal.getMobileNumber(), roomShortCode);
        }
    }


    private void sendLinkToPatient(String patientNumber, String roomShortCode) {

        String registeredMobileNumber = "+91" + patientNumber;

        //To be commented out to send SMS to the Patient...
        notificationService.sendTestSms(registeredMobileNumber, roomShortCode);
    }


    private ResponseEntity<Map<String, Object>> decryptPatientMobileNumber(AuthenticateUserFactory authenticateUserFactory, AuthenticateUserDTO userDTOData, EYUserDataModal userDataModal) {

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


    //Method to Validate Participants (Patient and MHP)...
    private ResponseEntity<Map<String, Object>> validateParticipants(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory) {
        return authenticationService.autheticateParticipantsData(userDTOData, authenticateUserFactory);
    }
}
