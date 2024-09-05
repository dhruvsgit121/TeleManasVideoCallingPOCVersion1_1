package com.example.ehrc.telemanas.Controller;


import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Service.NewServices.VideoCallService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/rooms")
public class VideoController {

    @Autowired
    private AuthenticateUserFactory authenticateUserFactory;

    @Autowired
    private VideoCallService videoCallService;

//    @Autowired
//    private SSEService sseService;

    @GetMapping("/helloworld")
    public String getVideoRoomDetails() {
        return "videoCallService.startVideoCall(roomDetailsRequest)!!!!!!";
    }

    @PostMapping("/getroomdetails")
    public ResponseEntity<Map<String, Object>> getVideoRoomDetails(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        //Setting the "hasJoined" Flag for Patient to TRUE...
        if (roomDetailsRequest.getIsMHP() == 0)
            videoCallService.saveIsActiveRoomOnJoinVideoCall(roomDetailsRequest);

        return videoCallService.startVideoCall(roomDetailsRequest);
    }

    @GetMapping("/getpatientjoinflag")
    public ResponseEntity<Map<String, Object>> getPatientRoomJoinDetails(@RequestParam String roomShortCode) {
        return videoCallService.getPatientJoinRoomStatusData(roomShortCode);
    }


    @GetMapping("/deactivateroom")
    public ResponseEntity<Map<String, Object>> deactivatedRequestedRoom(@RequestParam String roomShortCode) {
        return videoCallService.deactivateRequestedRoom(roomShortCode);
    }


    @RequestMapping("/resendlink")
    public ResponseEntity<Map<String, Object>> resendMeetingLink(@Valid @RequestBody AuthenticateUserDTO userDTOData,
                                                          @RequestHeader("Authorization") String bearerToken,
                                                          @RequestHeader(value = "Loggedin") String loggedIn) {

        System.out.println("Method called with : " + userDTOData.getRoomShortCode());

        Map<String, Object> data = new HashMap<>();

        String token = bearerToken.substring(7);

        //Adjusting The UserDTOData...
        userDTOData.setBearerToken(token);
        userDTOData.setLoggedInId(loggedIn);

        return videoCallService.decryptPatientMobileNumberForResendSMS(userDTOData, authenticateUserFactory, userDTOData.getRoomShortCode());
    }



    @PostMapping("/joinroom")
    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {
        return videoCallService.JoinVideoCall(roomDetailsRequest);
    }


    @PostMapping("/exitroom")
    public ResponseEntity<Map<String, Object>> leaveVideoCall(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {
        return videoCallService.leaveVideoCall(roomDetailsRequest);
    }


    @RequestMapping("/createroom")
    public ResponseEntity<Map<String, Object>> createRoom(@Valid @RequestBody AuthenticateUserDTO userDTOData,
                                                          @RequestHeader("Authorization") String bearerToken,
                                                          @RequestHeader(value = "Loggedin") String loggedIn
    ) {
        String token = bearerToken.substring(7);

        System.out.println("method called");

        //Adjusting The UserDTOData...
        userDTOData.setBearerToken(token);
        userDTOData.setLoggedInId(loggedIn);

        System.out.println("User data to be sent to api is : " + userDTOData);

        return videoCallService.createMeetingLink(userDTOData, authenticateUserFactory);
    }
}