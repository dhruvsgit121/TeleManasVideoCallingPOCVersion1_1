package com.example.ehrc.telemanas.Service.NewServices;

import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
import com.example.ehrc.telemanas.Model.UserIdentity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserIdentityService {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public boolean setUserIdentifyAsVerified(long serialID) {
        try {
            UserIdentity userIdentity = entityManager.find(UserIdentity.class, serialID);
            if (userIdentity != null) {
                userIdentity.setIdentityVerified(true);
                entityManager.merge(userIdentity);
                entityManager.flush();
                return true; // Update successful
            }
        } catch (Exception e) {
            System.out.println("Error updating participant with serial ID : " + serialID + " : "+ e.getMessage());
        }
        return false; // Participant not found or error occurred
    }


}
