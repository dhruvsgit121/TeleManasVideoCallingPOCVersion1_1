package com.example.ehrc.telemanas.UserRepository;

import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import com.example.ehrc.telemanas.Model.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserIdentityRepository extends JpaRepository<UserIdentity, Long> {

    @Query(value = "SELECT * FROM user_identity WHERE room_short_code = :roomShortCode", nativeQuery = true)
    UserIdentity findUserIdentityWith(@Param("roomShortCode") String roomShortCode);

}
