package com.example.ehrc.telemanas.Service.NewServices;


import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedParticipant;
import com.example.ehrc.telemanas.UserRepository.UpdatedParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdatedParticipantService {

    @Autowired
    private UpdatedParticipantRepository updatedParticipantRepository;

    public void saveUpdatedParticipantData(UpdatedParticipant participant) {
        updatedParticipantRepository.save(participant);
    }
}
