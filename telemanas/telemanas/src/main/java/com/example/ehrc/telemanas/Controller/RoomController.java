package com.example.ehrc.telemanas.Controller;


import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Service.*;
import com.example.ehrc.telemanas.Service.NewServices.VideoCallService;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private AuthenticateUserFactory authenticateUserFactory;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private VideoCallService videoCallService;

    @Autowired
    private SSEService sseService;

    @PostMapping("/getroomdetails")
    public ResponseEntity<Map<String, Object>> getVideoRoomDetails(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> roomServiceRequestData = roomService.generateVideoRoomDetailsResponseEntity(roomDetailsRequest);

        System.out.println("roomServiceRequestData" + roomServiceRequestData);

        if (roomServiceRequestData.containsKey("clientID")) {
            //User is a patient, who is joining the call...
            String clientID = roomServiceRequestData.get("clientID").toString();
            sseService.sendCustomMessage(clientID + "", "Hello patient joined the call...");
        }

        return new ResponseEntity(roomServiceRequestData, HttpStatus.OK);
    }


    @PostMapping("/joinroom")
    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();//new HashMap<>();
        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));
        System.out.println("Participant list is : " + participantsList);

        if (participantsList.size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        //Setting the Join Date of the user...
        Participant requestedParticipant = videoCallingUtilities.getRequestedUserAsPerRequest(roomDetailsRequest, participantsList);
        requestedParticipant.setJoinDate(videoCallingUtilities.getDateTimeWithOffset(0));

        participantService.saveParticipant(requestedParticipant);

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    @PostMapping("/exitroom")
    public ResponseEntity<Map<String, Object>> setExitRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();

        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));

        if (participantsList.size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        //Setting the Left Date of the user...
        Participant requestedParticipant = videoCallingUtilities.getRequestedUserAsPerRequest(roomDetailsRequest, participantsList);
        requestedParticipant.setLeftDate(videoCallingUtilities.getDateTimeWithOffset(0));

        participantService.saveParticipant(requestedParticipant);

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    @RequestMapping("/createroom")
    public ResponseEntity<Map<String, Object>> createRoomWith(@Valid @RequestBody AuthenticateUserDTO userDTOData,
                                                              @RequestHeader("Authorization") String bearerToken,
                                                              @RequestHeader(value = "Loggedin") String loggedIn
    ) {
        String token = bearerToken.substring(7);

        //Adjusting The UserDTOData...
        userDTOData.setBearerToken(token);
        userDTOData.setLoggedInId(loggedIn);

        System.out.println("User data to be sent to api is : " + userDTOData);

        return videoCallService.createMeetingLink(userDTOData, authenticateUserFactory);
    }
}