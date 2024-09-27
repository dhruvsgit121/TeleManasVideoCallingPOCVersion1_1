package com.example.ehrc.telemanas.Service.NewStructuredService;

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

}
