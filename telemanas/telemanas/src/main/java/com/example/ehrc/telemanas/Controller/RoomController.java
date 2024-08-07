package com.example.ehrc.telemanas.Controller;


import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.CreateRoomDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.Room;
import com.example.ehrc.telemanas.Model.User;
import com.example.ehrc.telemanas.Service.JWTTokenService;
import com.example.ehrc.telemanas.Service.ParticipantService;
import com.example.ehrc.telemanas.Service.RoomService;
import com.example.ehrc.telemanas.Service.UserService;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Value("${jwt.expirationOffSet}")
    private int expirationOffset;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipantService participantService;


    @Autowired
    private JWTTokenService jwtTokenService;

//    @PostMapping("/createroom")
//    public ResponseEntity<Room> createVideoCallingRoom() {
//
//        String roomID = videoCallingUtilities.generateRandomString(20);
//        String roomShortCode = videoCallingUtilities.generateRandomString(20);
//
//        Room roomData = new Room(roomID, videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomShortCode);
//        Room savedRoomData = roomService.saveRoom(roomData);
//
//        return new ResponseEntity(savedRoomData, HttpStatus.OK);
//    }


//    @PostMapping("/createvideocallingroom")
//    public ResponseEntity<String> createVideoCallingRoomWithRequest(@RequestBody CreateRoomDTO createRoomData) {
//
//        System.out.println("request parameters are MHP ID is : " + createRoomData.getMhpID() + " patient id is  : " + createRoomData.getPatientId());
//
//
//        return new ResponseEntity<>("Success", HttpStatus.OK);
//    }


    @PostMapping("/createroom")
    public ResponseEntity<Room> createVideoCallingRoomDummy(@Valid @RequestBody CreateRoomDTO createRoomData) {

        if (createRoomData.getPatientId() == null) {
            throw new ValidationMessagesException("Patient ID cannot be null.");
        } else if (createRoomData.getMhpID() == null) {
            throw new ValidationMessagesException("MHP ID cannot be null.");
        }

        Optional<User> MHPUser = userService.getUserByID(createRoomData.getMhpID(), User.UserRole.MHP.toString());//Optional.ofNullable(userService.getUserByID(createRoomData.getMhpID()));
        User extractedMPHUser, extractedPatientUser;

        Optional<User> PatientUser = userService.getUserByID(createRoomData.getPatientId(), User.UserRole.PATIENT.toString());//Optional.ofNullable(userService.getUserByID(createRoomData.getMhpID()));

        if (MHPUser.isPresent()) {
            extractedMPHUser = MHPUser.get();
        } else {
            throw new ValidationMessagesException("MHP ID with this role does not exist.");
        }

        if (PatientUser.isPresent()) {
            extractedPatientUser = PatientUser.get();
        } else {
            throw new ValidationMessagesException("Patient ID with this role does not exist.");
        }


        LocalDateTime expiryDate = videoCallingUtilities.getDateTimeWithOffset(1800);

        System.out.println("Exppiration time is : " + expiryDate);

        System.out.println("Room code list is : " + participantService.getRoomShortCodeWith(createRoomData.getMhpID(), createRoomData.getPatientId(), expiryDate));

        ArrayList<String> roomShortCodesList = new ArrayList<>(participantService.getRoomShortCodeWith(createRoomData.getMhpID(), createRoomData.getPatientId(), expiryDate));


        System.out.println("roomShortCodesList : " + roomShortCodesList);

        //In case we have Already Room ID then we won't create it And will return the already existing ROOM CODE...
        if (roomShortCodesList.size() > 0) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("roomCode", roomShortCodesList.get(0));
            return new ResponseEntity(responseMap, HttpStatus.OK);
        }


        String roomID = videoCallingUtilities.generateRandomString(20);
        String roomShortCode = videoCallingUtilities.generateRandomString(20);

        Room roomData = new Room(roomID, videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomShortCode);


        String doctorJWTToken = jwtTokenService.generateJWTToken(extractedMPHUser.getUserName(), extractedMPHUser.getUserEmail(), roomID, true);
        String patientJWTToken = jwtTokenService.generateJWTToken(extractedPatientUser.getUserName(), extractedPatientUser.getUserEmail(), roomID, false);


        Participant mhpUser = new Participant(null, null, doctorJWTToken, createRoomData.getMhpID());
        Participant patientUser = new Participant(null, null, patientJWTToken, createRoomData.getPatientId());

        roomData.addParticipant(mhpUser);
        roomData.addParticipant(patientUser);

        Room savedRoomData = roomService.saveRoom(roomData);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("roomCode", savedRoomData.getRoomShortCode());

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }

    @PostMapping("/getroomdetails")
    public ResponseEntity<Map<String, Object>> getVideoRoomDetails(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

//        Map<String, Object> roomResponseData = new HashMap<>();

        System.out.println("roomShortCode : " + roomDetailsRequest);

        Room roomDetails = roomService.getRoomDetailsWith(roomDetailsRequest.getRoomShortCode());

        if (!roomDetails.isActive() || roomDetails.getParticipants().size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        ArrayList<Participant> participantsList = new ArrayList<>(roomDetails.getParticipants());

        Map<String, Object> responseData = new HashMap<>();

        Participant firstParticipant = participantsList.get(0);
        Participant secondParticipant = participantsList.get(1);

        System.out.println("eneter here " + firstParticipant.getParticipantId());
        System.out.println("eneter here " + secondParticipant.getParticipantId());

        responseData.put("roomID", roomDetails.getRoomId());

        User firstUser = userService.getUserByID(firstParticipant.getParticipantId());

        if ((roomDetailsRequest.getIsMHP() == 1 && firstUser.getUserRole().equals(User.UserRole.MHP.toString())) || (roomDetailsRequest.getIsMHP() != 1 && firstUser.getUserRole().equals(User.UserRole.PATIENT))) {

            responseData.put("jwtToken", firstParticipant.getJwtToken());
            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), firstParticipant.getJwtToken()));
        } else {

            responseData.put("jwtToken", secondParticipant.getJwtToken());
            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), secondParticipant.getJwtToken()));

        }
        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    @PostMapping("/joinroom")
    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> responseData = new HashMap<>();

        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));

        if (participantsList.size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        Participant firstParticipant = participantService.getParticipantByID(participantsList.get(0));
        Participant secondParticipant = participantService.getParticipantByID(participantsList.get(1));

        User firstUser = userService.getUserByID(firstParticipant.getParticipantId());

        if ((roomDetailsRequest.getIsMHP() == 1 && firstUser.getUserRole().equals(User.UserRole.MHP)) || (roomDetailsRequest.getIsMHP() != 1 && firstUser.getUserRole().equals(User.UserRole.PATIENT))) {
            firstParticipant.setJoinDate(videoCallingUtilities.getDateTimeWithOffset(0));
            participantService.saveParticipant(firstParticipant);
        } else {
            secondParticipant.setJoinDate(videoCallingUtilities.getDateTimeWithOffset(0));
            participantService.saveParticipant(secondParticipant);
        }

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);
    }




    @PostMapping("/exitroom")
    public ResponseEntity<Map<String, Object>> setExitRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> responseData = new HashMap<>();

        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));

        if (participantsList.size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        Participant firstParticipant = participantService.getParticipantByID(participantsList.get(0));
        Participant secondParticipant = participantService.getParticipantByID(participantsList.get(1));

        User firstUser = userService.getUserByID(firstParticipant.getParticipantId());

        if ((roomDetailsRequest.getIsMHP() == 1 && firstUser.getUserRole().equals(User.UserRole.MHP)) || (roomDetailsRequest.getIsMHP()  != 1 && firstUser.getUserRole().equals(User.UserRole.PATIENT))) {
            firstParticipant.setLeftDate(videoCallingUtilities.getDateTimeWithOffset(0));
            participantService.saveParticipant(firstParticipant);
        } else {
            secondParticipant.setLeftDate(videoCallingUtilities.getDateTimeWithOffset(0));
            participantService.saveParticipant(secondParticipant);
        }

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);
    }
}