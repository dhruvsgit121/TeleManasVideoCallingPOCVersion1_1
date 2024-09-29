package com.example.ehrc.telemanas.Service.NewStructuredService;

import com.example.ehrc.telemanas.DTO.CallStartDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.ResendVideoCallLinkDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.SendPrescriptionDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.VerifyUserIdentityDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.DTO.VideoCallEventsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class NewVideoService {

    @Autowired
    private RoomManagerService roomManagerService;

    @Autowired
    private NewEventService eventService;

    @Autowired
    private AuthenticationService authenticationService;

    public ResponseEntity<Map<String, Object>> createMeetingLink(AuthenticateUserDTO userAuthorisationDataDTO) {
        return roomManagerService.createMeetingLink(userAuthorisationDataDTO, authenticationService);
    }

    public ResponseEntity<Map<String, Object>> resendVideoCallLink(AuthenticateUserDTO userDTOData, String encryptedPhoneNumber) {
        return roomManagerService.resendVideoCallLink(userDTOData, authenticationService, encryptedPhoneNumber);
    }

    public ResponseEntity<Map<String, Object>> resendPrescriptionLink(AuthenticateUserDTO userDTOData, SendPrescriptionDTO sendPrescriptionData) {
        return roomManagerService.resendPrescriptionLink(userDTOData, authenticationService, sendPrescriptionData);
    }

    public ResponseEntity<Map<String, Object>> uploadFile(MultipartFile file, String roomShortCode) {
        return roomManagerService.uploadFile(file, roomShortCode);
    }

    public ResponseEntity<Map<String, Object>> getPrescriptionDetails(String roomShortCode) {
        return roomManagerService.getPrescriptionDetails(roomShortCode);
    }

    public ResponseEntity<Map<String, Object>> verifyUserIdentity(VerifyUserIdentityDTO verifyUserIdentityDTO) {
        return roomManagerService.verifyUserIdentity(verifyUserIdentityDTO);
    }

//    public ResponseEntity<Map<String, Object>> resendVideoCallLink(AuthenticateUserDTO userAuthorisationDataDTO, AuthenticationService authenticationService) {

    public ResponseEntity<Map<String, Object>> deactivateRequestedRoom(String roomShortCode) {
        return roomManagerService.deactivateRequestedRoom(roomShortCode);
    }


    //Method to Join Video Call...
    public ResponseEntity<Map<String, Object>> JoinVideoCall(CallStartDTO callStartDTO) {
        return roomManagerService.JoinVideoCall(callStartDTO);
    }

    //Method to Join Video Call...
    public ResponseEntity<Map<String, Object>> JoinPatientVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        return roomManagerService.JoinPatientVideoCall(roomDetailsRequest);
    }


    //Method to Join Video Call...
    public ResponseEntity<Map<String, Object>> LeaveMHPVideoCall(CallStartDTO callStartDTO) {
        return roomManagerService.leaveVideoCall(callStartDTO);
    }

    //Method to Join Video Call...
    public ResponseEntity<Map<String, Object>> PatientJoinVideoCall(String roomShortCode) {

        eventService.getVideoConsultationMasterRecord("MHP Joined the room.");

        return roomManagerService.patientJoinCallFlagData(roomShortCode);
    }


    public ResponseEntity<Map<String, Object>> saveVideoCallEvents(VideoCallEventsDTO videoCallEventsDTO) {
        return eventService.saveVideoCallEvent(videoCallEventsDTO);
    }

//    //Method to Join Video Call...
//    public ResponseEntity<Map<String, Object>> LeavePatientVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
//        return roomManagerService.leaveVideoCall(roomDetailsRequest);
//    }

}
