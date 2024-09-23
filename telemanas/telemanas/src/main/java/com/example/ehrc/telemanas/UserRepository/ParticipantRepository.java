package com.example.ehrc.telemanas.UserRepository;

import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface ParticipantRepository extends JpaRepository<Participant, Long> {

//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE participant SET has_joined_room = TRUE WHERE serial_id = :serialID", nativeQuery = true)
//    int setParticipantHasJoinedRoomFlag(@Param("serialID") Long serialID);

}
