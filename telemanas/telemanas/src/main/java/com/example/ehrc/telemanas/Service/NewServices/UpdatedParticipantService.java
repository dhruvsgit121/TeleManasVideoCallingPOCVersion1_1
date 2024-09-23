package com.example.ehrc.telemanas.Service.NewServices;


import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
import com.example.ehrc.telemanas.UserRepository.ParticipantRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UpdatedParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void saveUpdatedParticipantData(Participant participant) {
        participantRepository.save(participant);
    }



    @Autowired
    private EntityManager entityManager;

    @Transactional
    public boolean markParticipantAsJoined(long serialID) {
        try {
            Participant participant = entityManager.find(Participant.class, serialID);
            if (participant != null) {
                participant.setHasJoinedRoom(true);
                entityManager.merge(participant);
                entityManager.flush();
                return true; // Update successful
            }
        } catch (Exception e) {
            System.out.println("Error updating participant with serial ID : " + serialID + " : "+ e.getMessage());
        }
        return false; // Participant not found or error occurred
//        Participant participant = entityManager.find(Participant.class, serialID);
//        if (participant != null) {
//            participant.setHasJoinedRoom(true);
//            entityManager.merge(participant);
//            return true; // Update successful
//        }
//        return false; // Participant not found
    }
}
