package com.example.ehrc.telemanas.UserRepository;

import com.example.ehrc.telemanas.Model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

//import java.util.Locale;
//import java.util.regex.Pattern;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query(value = "SELECT DISTINCT r.room_short_code FROM participant p JOIN room r ON p.room_id = r.serial_id WHERE r.is_active = true AND r.expiration_date > :expirationDate AND p.participant_id = :mhpId OR p.participant_id = :patientId", nativeQuery = true)
    List<String> findRoomShortCodeWith(@Param("mhpId") Long mhpId, @Param("patientId") Long patientId, @Param("expirationDate") LocalDateTime expirationDate);

}
