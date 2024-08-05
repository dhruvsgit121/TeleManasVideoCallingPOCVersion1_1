package com.example.ehrc.telemanas.Controller;


import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.User;
import com.example.ehrc.telemanas.Service.ParticipantService;
//import com.example.ehrc.telemanas.Service.RoomService;
import com.example.ehrc.telemanas.Service.UserService;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Value("${jwt.expirationOffSet}")
    private long expirationOffset;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private UserService userService;

    @GetMapping("/createparticipant")
    public ResponseEntity<Participant> addParticipant() {

        User desiredUser = userService.getUserByID(1);

        Participant pariticpantToBeAdded = new Participant("kldaehfjkees1", videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), "slqkjdeefklfhseekj111dhkjsfd");
        pariticpantToBeAdded.setUser(desiredUser);

        Participant addedParticipant = participantService.saveParticipant(pariticpantToBeAdded);
        return new ResponseEntity<>(pariticpantToBeAdded, HttpStatus.CREATED);
    }
}
