package com.example.ehrc.telemanas.Controller;


import com.example.ehrc.telemanas.DTO.CreateRoomDTO;
import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.Room;
import com.example.ehrc.telemanas.Service.RoomService;
//import com.example.ehrc.telemanas.Service.UserService;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Value("${jwt.expirationOffSet}")
    private int expirationOffset;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private RoomService roomService;

    @PostMapping("/createroom")
    public ResponseEntity<Room> createVideoCallingRoom() {

        String roomID = videoCallingUtilities.generateRandomString(20);
        String roomShortCode = videoCallingUtilities.generateRandomString(20);

        Room roomData = new Room(roomID, videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomShortCode);
        Room savedRoomData = roomService.saveRoom(roomData);

        return new ResponseEntity(savedRoomData, HttpStatus.OK);
    }


    @PostMapping("/createvideocallingroom")
    public ResponseEntity<String> createVideoCallingRoomWithRequest(@RequestBody CreateRoomDTO createRoomData){

        System.out.println("request parameters are MHP ID is : " + createRoomData.getMhpID() + " patient id is  : " + createRoomData.getPatientId());


        return new ResponseEntity<>("Success", HttpStatus.OK);
    }




    @PostMapping("/createroomdummy")
    public ResponseEntity<Room> createVideoCallingRoomDummy(@RequestBody CreateRoomDTO createRoomData) {

        String roomID = videoCallingUtilities.generateRandomString(20);
        String roomShortCode = videoCallingUtilities.generateRandomString(20);

        Room roomData = new Room(roomID, videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomShortCode);
        //Room savedRoomData = roomService.saveRoom(roomData);


        Participant mhpUser = new Participant(null, null, "Jwttoken1");
        mhpUser.setParticipantId(createRoomData.getMhpID());
        Participant patientUser = new Participant(null, null, "patientJwttoken1");
        patientUser.setParticipantId(createRoomData.getPatientId());
        //roomData.setParticipants(Set.of(mhpUser, patientUser));

        roomData.addParticipant(mhpUser);
        roomData.addParticipant(patientUser);

        Room savedRoomData = roomService.saveRoom(roomData);
        System.out.println("request parameters are MHP ID is : " + createRoomData.getMhpID() + " patient id is  : " + createRoomData.getPatientId());

        return new ResponseEntity(savedRoomData, HttpStatus.OK);
    }
}
