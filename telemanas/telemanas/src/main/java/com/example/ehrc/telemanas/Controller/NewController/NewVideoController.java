package com.example.ehrc.telemanas.Controller.NewController;


import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.Service.NewServices.VideoCallService;
import com.example.ehrc.telemanas.Service.NewStructuredService.NewVideoService;
import com.example.ehrc.telemanas.Service.NewStructuredService.RoomManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class NewVideoController {

    @Autowired
    private RoomManagerService roomManagerService;

    @Autowired
    private NewVideoService videoService;


    @RequestMapping("/createroom")
    public ResponseEntity<Map<String, Object>> createRoom(@Valid @RequestBody AuthenticateUserDTO userAuthorisationDataDTO,
                                                          @RequestHeader("Authorization") String bearerToken,
                                                          @RequestHeader(value = "Loggedin") String loggedIn
    ) {
        setAuthorizationData(userAuthorisationDataDTO, bearerToken, loggedIn);
        return videoService.createMeetingLink(userAuthorisationDataDTO);
    }

    @GetMapping("/deactivateroom")
    public ResponseEntity<Map<String, Object>> deactivatedRequestedRoom(@RequestParam String roomShortCode) {
        System.out.println("deactivateroom requetsed code is : " + roomShortCode);
        return videoService.deactivateRequestedRoom(roomShortCode);
    }

    private void setAuthorizationData(AuthenticateUserDTO userDTOData, String bearerToken, String loggedIn) {

        String token = bearerToken.substring(7);

        //Adjusting The UserDTOData...
        userDTOData.setBearerToken(token);
        userDTOData.setLoggedInId(loggedIn);
    }
}
