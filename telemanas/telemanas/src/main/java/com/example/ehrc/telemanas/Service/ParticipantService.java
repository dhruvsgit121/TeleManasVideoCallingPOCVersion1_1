package com.example.ehrc.telemanas.Service;


import com.example.ehrc.telemanas.Model.Participant;
//import com.example.ehrc.telemanas.Model.Room;
import com.example.ehrc.telemanas.UserRepository.ParticipantRepository;
//import com.example.ehrc.telemanas.UserRepository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParticipantService {


    @Autowired
    private ParticipantRepository participantRepository;

    public Participant saveParticipant(Participant participant){
        return participantRepository.save(participant);
    }

    public List<String> getRoomShortCodeWith(Long MHPId, Long patientID, LocalDateTime expirationDate){
        return participantRepository.findRoomShortCodeWith(MHPId, patientID, expirationDate);
    }
}
