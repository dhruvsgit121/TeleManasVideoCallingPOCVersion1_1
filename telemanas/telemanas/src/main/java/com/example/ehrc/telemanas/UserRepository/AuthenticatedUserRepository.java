package com.example.ehrc.telemanas.UserRepository;

import com.example.ehrc.telemanas.Model.UpdatedModels.AuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AuthenticatedUserRepository extends JpaRepository<AuthenticatedUser, Long> {

    @Query(value = "SELECT * FROM authenticated_user WHERE participant_id = :participantId", nativeQuery = true)
    AuthenticatedUser findAuthenticatedUser(@Param("participantId") String userID);

}
