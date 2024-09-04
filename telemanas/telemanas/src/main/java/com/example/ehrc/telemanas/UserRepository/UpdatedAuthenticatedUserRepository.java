package com.example.ehrc.telemanas.UserRepository;

import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedAuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UpdatedAuthenticatedUserRepository extends JpaRepository<UpdatedAuthenticatedUser, Long> {

    @Query(value = "SELECT * FROM updated_autheticated_user WHERE participant_id = :participantId", nativeQuery = true)
    UpdatedAuthenticatedUser findAuthenticatedUser(@Param("participantId") String userID);

}
