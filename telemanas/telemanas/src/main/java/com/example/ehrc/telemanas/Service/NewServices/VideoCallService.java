package com.example.ehrc.telemanas.Service.NewServices;

import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUser;
import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Model.EYDataModel.MHPDataModal;
import com.example.ehrc.telemanas.Model.EYDataModel.PatientDataModal;
import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedAuthenticatedUser;
import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedParticipant;
import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedRoom;
import com.example.ehrc.telemanas.Service.ParticipantService;
import com.example.ehrc.telemanas.Service.RoomService;
import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoCallService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UpdatedRoomService updatedRoomService;

    @Autowired
    private UpdatedParticipantService updatedParticipantService;

    @Autowired
    private RoomService roomService;

    public ResponseEntity<Map<String, Object>> startVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        return roomService.startVideoCall(roomDetailsRequest);
    }

    public ResponseEntity<Map<String, Object>> getPatientJoinRoomStatusData(String roomShortCode) {
        return updatedRoomService.getPatientJoinData(roomShortCode);
    }


    public ResponseEntity<Map<String, Object>> leaveVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        return roomService.exitRoom(roomDetailsRequest);
    }

    public ResponseEntity<Map<String, Object>> JoinVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        return roomService.joinRoom(roomDetailsRequest);
    }


    public ResponseEntity<Map<String, Object>> deactivateRequestedRoom(String roomShortCode) {
        return roomService.deactivateRequestedRoom(roomShortCode);
    }


    public void saveIsActiveRoomOnJoinVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {

        UpdatedRoom updatedRoom = updatedRoomService.findRoomDetailsWith(roomDetailsRequest.getRoomShortCode());//participantService.getPatientParticipant(roomDetailsRequest.getRoomShortCode());

        if (updatedRoom.getParticipants().size() == 2) {

            UpdatedParticipant firstParticipant = updatedRoom.getParticipants().get(0);
            UpdatedParticipant secondParticipant = updatedRoom.getParticipants().get(1);

            //Update the Has Joined Room Flag for Patient to TRUE...
            UpdatedParticipant patientParticipantData = firstParticipant.getAuthenticatedUser().getUserRole().equals(Participant.UserRole.PATIENT) ? firstParticipant : secondParticipant;
            patientParticipantData.setHasJoinedRoom(true);
            updatedParticipantService.saveUpdatedParticipantData(patientParticipantData);
        }
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
        return videoCallRoomData;
    }

    private void sendMessageAfterParsingVideoCallRoomData(ResponseEntity<Map<String, Object>> videoCallRoomData, PatientDataModal userDataModal) {
        if (videoCallRoomData.hasBody()) {
            String roomShortCode = videoCallRoomData.getBody().get("roomCode").toString();
            sendSMSToPatient(userDataModal.getMobileNumber(), roomShortCode);
        }
    }


    public void sendSMSToPatient(String mobileNumber, String roomShortCode){
        sendLinkToPatient(mobileNumber, roomShortCode);
    }

    private void sendLinkToPatient(String patientNumber, String roomShortCode) {
        String registeredMobileNumber = "+91" + patientNumber;
        //To be commented out to send SMS to the Patient...
        notificationService.sendTestSms(registeredMobileNumber, roomShortCode);
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

        UpdatedRoom roomData = updatedRoomService.findRoomDetailsWith(roomShortCode);

//        System.out.println("room is  = " + roomData);

//        if(roomData == null)
//            System.out.println("room data is null!!1");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put(VideoCallingAPIConstants.isErrorFlagValue, false);

        if (roomData == null || roomData.getParticipants().size() != 2) {
            responseData.put(VideoCallingAPIConstants.isErrorFlagValue, true);
            responseData.put(VideoCallingAPIConstants.errorMessageValue, "Room you requested didn't exists.");
            return new ResponseEntity<>(responseData, HttpStatus.SEE_OTHER);
        }


        if (!roomData.isActive()) {
            responseData.put(VideoCallingAPIConstants.isErrorFlagValue, true);
            responseData.put(VideoCallingAPIConstants.errorMessageValue, "Room you requested is not active. Please try again.");
            return new ResponseEntity<>(responseData, HttpStatus.SEE_OTHER);
        }

        UpdatedAuthenticatedUser patientData = roomData.getParticipants().get(0).getAuthenticatedUser().getUserRole().equals(Participant.UserRole.PATIENT) ? roomData.getParticipants().get(0).getAuthenticatedUser() : roomData.getParticipants().get(1).getAuthenticatedUser();

        String encryptedPhoneNumber = patientData.getDecryptedMobileNumber();

        //Decrypting Mobile Number of The Patient...
        ResponseEntity<Map<String, Object>> decryptMobileData = authenticateUserFactory.decryptUserPhoneNumber(userDTOData, encryptedPhoneNumber);
        if (decryptMobileData.getStatusCode() != HttpStatus.OK)
            return decryptMobileData;

        System.out.println("Data Recieved is in mobile decrypt is : " + decryptMobileData.getBody());

        String errorMessage = "Some error Occurred. Please try again later.";

        if(decryptMobileData.hasBody()){
            if(decryptMobileData.getBody().containsKey("responsePhoneNo")){
                String mobileNumber = decryptMobileData.getBody().get("responsePhoneNo").toString();
                sendSMSToPatient(mobileNumber, roomShortCode);
                return new ResponseEntity<>(responseData, HttpStatus.OK);
            }
            if(decryptMobileData.getBody().containsKey("message") && decryptMobileData.getBody().get("message") != null){
                errorMessage = decryptMobileData.getBody().get("message").toString();
            }
        }
        responseData.put(VideoCallingAPIConstants.errorMessageValue, errorMessage);
        return new ResponseEntity<>(responseData, HttpStatus.SEE_OTHER);
    }


    //Method to Validate Participants (Patient and MHP)...
    private ResponseEntity<Map<String, Object>> validatePatientData(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory) {
        return authenticationService.autheticatePatientData(userDTOData, authenticateUserFactory);
    }

    private ResponseEntity<Map<String, Object>> validateMHPData(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory) {
        return authenticationService.autheticateMHPData(userDTOData, authenticateUserFactory);
    }

}
