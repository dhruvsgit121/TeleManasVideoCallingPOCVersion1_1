package com.example.ehrc.telemanas.Controller;


import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Service.*;
import com.example.ehrc.telemanas.Service.NewServices.VideoCallService;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
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
    private VideoCallingUtilities videoCallingUtilities;

//    @Autowired
//    private RoomService roomService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private VideoCallService videoCallService;

//    @Autowired
//    private SSEService sseService;

    @GetMapping("/helloworld")
    public String getVideoRoomDetails() {
        return "videoCallService.startVideoCall(roomDetailsRequest)";
    }

    @PostMapping("/getroomdetails")
    public ResponseEntity<Map<String, Object>> getVideoRoomDetails(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {
        return videoCallService.startVideoCall(roomDetailsRequest);
    }


    @PostMapping("/joinroom")
    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        return videoCallService.JoinVideoCall(roomDetailsRequest);

//        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();//new HashMap<>();
//        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));
//        System.out.println("Participant list is : " + participantsList);
//
//        if (participantsList.size() != 2) {
//            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
//        }
//
//        //Setting the Join Date of the user...
//        Participant requestedParticipant = videoCallingUtilities.getRequestedUserAsPerRequest(roomDetailsRequest, participantsList);
//        requestedParticipant.setJoinDate(videoCallingUtilities.getDateTimeWithOffset(0));
//
//        participantService.saveParticipant(requestedParticipant);
//
//        responseData.put("message", "success");
//        return new ResponseEntity(responseData, HttpStatus.OK);
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