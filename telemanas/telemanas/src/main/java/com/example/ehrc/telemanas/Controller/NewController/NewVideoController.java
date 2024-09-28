package com.example.ehrc.telemanas.Controller.NewController;

import com.example.ehrc.telemanas.DTO.CallStartDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
//import com.example.ehrc.telemanas.Service.NewServices.VideoCallService;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Service.NewStructuredService.NewVideoService;
import com.example.ehrc.telemanas.Service.NewStructuredService.RoomManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class NewVideoController {

    @Autowired
    private RoomManagerService roomManagerService;

    @Autowired
    private NewVideoService videoService;


    //###################### ROOM RELATED API's ######################

    @RequestMapping("/createroom")
    public ResponseEntity<Map<String, Object>> createRoom(@Valid @RequestBody AuthenticateUserDTO userAuthorisationDataDTO,
                                                          @RequestHeader("Authorization") String bearerToken,
                                                          @RequestHeader(value = "Loggedin") String loggedIn
    ) {
        setAuthorizationData(userAuthorisationDataDTO, bearerToken, loggedIn);
        return videoService.createMeetingLink(userAuthorisationDataDTO);
    }


    //###################### VIDEO CALL RELATED API's ######################

    @PostMapping("/joinroom")
    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody CallStartDTO callStartDTO, @RequestHeader("Authorization") String bearerToken,
                                                               @RequestHeader(value = "Loggedin") String loggedIn) {
        setAuthorizationData(callStartDTO, bearerToken, loggedIn);
        return videoService.JoinVideoCall(callStartDTO);
    }


    @PostMapping("/patientjoinroom")
    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {
        return videoService.JoinPatientVideoCall(roomDetailsRequest);//videoCallService.JoinVideoCall(roomDetailsRequest);
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

    private void setAuthorizationData(CallStartDTO callStartDTO, String bearerToken, String loggedIn) {
        String token = bearerToken.substring(7);

        //Adjusting The UserDTOData...
        callStartDTO.setBearerToken(token);
        callStartDTO.setLoggedInId(loggedIn);
    }
}
