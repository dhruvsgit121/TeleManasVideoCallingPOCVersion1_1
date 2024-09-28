package com.example.ehrc.telemanas.Service.NewStructuredService;

import com.example.ehrc.telemanas.DTO.CallStartDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NewVideoService {

    @Autowired
    private RoomManagerService roomManagerService;

    @Autowired
    private AuthenticationService authenticationService;

    public ResponseEntity<Map<String, Object>> createMeetingLink(AuthenticateUserDTO userAuthorisationDataDTO) {
        return roomManagerService.createMeetingLink(userAuthorisationDataDTO, authenticationService);
    }

    public ResponseEntity<Map<String, Object>> deactivateRequestedRoom(String roomShortCode) {
        return roomManagerService.deactivateRequestedRoom(roomShortCode);
    }


    //Method to Join Video Call...
    public ResponseEntity<Map<String, Object>> JoinVideoCall(CallStartDTO callStartDTO) {

        return roomManagerService.JoinVideoCall(callStartDTO);
//        ResponseEntity<Map<String, Object>> responseData = eventService.callStartSaveData(callStartDTO);
//        if (responseData.getStatusCode() != HttpStatus.OK)
//            return responseData;
//
//        String eventDescription = (callStartDTO.getIsMHP() == 1) ? "MHP Started the video call" : "Patient Started the video call";
//        ResponseEntity<Map<String, Object>> eventServiceResponseData = eventService.saveEventData(callStartDTO.getRoomShortCode(), eventDescription);
//
//        boolean isErrorPresent = (boolean) (eventServiceResponseData.getBody().get("isErrorPresent"));
//
//        if (isErrorPresent)
//            return eventServiceResponseData;
//
//        return roomService.joinRoom(callStartDTO);
    }


}
