package com.example.ehrc.telemanas.Controller;


import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.CreateRoomDTO;
import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.Room;
//import com.example.ehrc.telemanas.Model.User;
import com.example.ehrc.telemanas.Model.User;
import com.example.ehrc.telemanas.Service.JWTTokenService;
import com.example.ehrc.telemanas.Service.RoomService;
//import com.example.ehrc.telemanas.Service.UserService;
import com.example.ehrc.telemanas.Service.UserService;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
//import java.util.Optional;
//import java.util.Set;

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
    private JWTTokenService jwtTokenService;

    @PostMapping("/createroom")
    public ResponseEntity<Room> createVideoCallingRoom() {

        String roomID = videoCallingUtilities.generateRandomString(20);
        String roomShortCode = videoCallingUtilities.generateRandomString(20);

        Room roomData = new Room(roomID, videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomShortCode);
        Room savedRoomData = roomService.saveRoom(roomData);

        return new ResponseEntity(savedRoomData, HttpStatus.OK);
    }


    @PostMapping("/createvideocallingroom")
    public ResponseEntity<String> createVideoCallingRoomWithRequest(@RequestBody CreateRoomDTO createRoomData) {

        System.out.println("request parameters are MHP ID is : " + createRoomData.getMhpID() + " patient id is  : " + createRoomData.getPatientId());


        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


    @PostMapping("/createroomdummy")
    public ResponseEntity<Room> createVideoCallingRoomDummy(@Valid @RequestBody CreateRoomDTO createRoomData) {

        if (createRoomData.getPatientId() == null) {
            throw new ValidationMessagesException("Patient ID cannot be null.");
        }else if(createRoomData.getMhpID() == null){
            throw new ValidationMessagesException("MHP ID cannot be null.");
        }

        Optional<User> MHPUser = userService.getUserByID(createRoomData.getMhpID(), User.UserRole.MHP.toString());//Optional.ofNullable(userService.getUserByID(createRoomData.getMhpID()));
        User extractedMPHUser, extractedPatientUser ;

        Optional<User> PatientUser =  userService.getUserByID(createRoomData.getPatientId(), User.UserRole.PATIENT.toString());//Optional.ofNullable(userService.getUserByID(createRoomData.getMhpID()));

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


        String roomID = videoCallingUtilities.generateRandomString(20);
        String roomShortCode = videoCallingUtilities.generateRandomString(20);

        Room roomData = new Room(roomID, videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomShortCode);

        Map<String, Object> jwtTokenData = jwtTokenService.generateUserJWTTokenData(extractedMPHUser.getUserName(), extractedMPHUser.getUserEmail(), extractedMPHUser.getContactNumber(), extractedPatientUser.getUserName(), extractedPatientUser.getUserEmail(), extractedPatientUser.getContactNumber());

        System.out.println( "JWT token dtaa is : " + jwtTokenData);

        Participant mhpUser = new Participant(null, null, jwtTokenData.get("doctorJWTToken").toString(),createRoomData.getMhpID());
        Participant patientUser = new Participant(null, null, jwtTokenData.get("patientJWTToken").toString(), createRoomData.getPatientId());

        roomData.addParticipant(mhpUser);
        roomData.addParticipant(patientUser);

        Room savedRoomData = roomService.saveRoom(roomData);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("roomCode", savedRoomData.getRoomShortCode());

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }
}
