package com.example.ehrc.telemanas.Service.NewStructuredService;

//import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import java.util.HashMap;
import java.util.Map;

@Service
public class NewVideoService {

    @Autowired
    private RoomManagerService roomManagerService;

    @Autowired
    private AuthenticationService authenticationService;


    public ResponseEntity<Map<String, Object>> createMeetingLink(AuthenticateUserDTO userAuthorisationDataDTO) {

//        ResponseEntity<Map<String, Object>> patientAuthenticationResponseData = authenticationService.authenticatePatientData(userAuthorisationDataDTO, au);//authenticationService.autheticateParticipantsData(userDTOData, authenticateUserFactory);
//
//        if (patientAuthenticationResponseData.getStatusCode() != HttpStatus.OK)
//            return patientAuthenticationResponseData;
//





        return roomManagerService.createMeetingLink(userAuthorisationDataDTO, authenticationService);

//        ResponseEntity<Map<String, Object>> patientMobileDecryptionResponseData = decryptPatientMobileNumber(authenticateUserFactory, userDTOData, patientUserDataModal);
//
//        if (patientMobileDecryptionResponseData != null) {
//            return patientMobileDecryptionResponseData;
//        }
//
//        ResponseEntity<Map<String, Object>> videoCallRoomData = roomService.createRoom(userDTOData, patientUserDataModal, mhpDataModal, roomService);
//        sendMessageAfterParsingVideoCallRoomData(videoCallRoomData, patientUserDataModal);
//
//        if (videoCallRoomData.hasBody() && videoCallRoomData.getBody().get("roomCode") != null) {
//            String eventDescription = "MHP created the room.";
//            ResponseEntity<Map<String, Object>> eventServiceResponseData = eventService.saveEventData(videoCallRoomData.getBody().get("roomCode").toString(), eventDescription);
//            boolean isErrorPresent = (boolean) (eventServiceResponseData.getBody().get("isErrorPresent"));
//            if (isErrorPresent)
//                return eventServiceResponseData;
//        }
//        return videoCallRoomData;

//        Map<String, Object> responseMap = new HashMap<>();
//        return new ResponseEntity<>(responseMap, HttpStatus.OK);

    }


}
