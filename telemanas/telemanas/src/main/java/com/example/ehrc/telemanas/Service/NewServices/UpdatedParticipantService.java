package com.example.ehrc.telemanas.Service.NewServices;


import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
import com.example.ehrc.telemanas.UserRepository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UpdatedParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void saveUpdatedParticipantData(Participant participant) {
        participantRepository.save(participant);
    }

}
