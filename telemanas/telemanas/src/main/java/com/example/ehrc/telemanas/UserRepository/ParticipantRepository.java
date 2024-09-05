//package com.example.ehrc.telemanas.UserRepository;
//
////import com.example.ehrc.telemanas.Model.Participant;
////import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import java.time.LocalDateTime;
//import java.util.List;
//
//
//public interface ParticipantRepository extends JpaRepository<Participant, Long> {
//
//    @Query(value = "SELECT DISTINCT r.room_short_code FROM participant p JOIN room r ON p.room_id = r.serial_id WHERE r.is_active = TRUE AND r.expiration_date > :expirationDate AND (p.participant_id = :mhpId OR p.participant_id = :patientId)", nativeQuery = true)
//    List<String> findRoomShortCodeWith(@Param("mhpId") String mhpId, @Param("patientId") String patientId, @Param("expirationDate") LocalDateTime expirationDate);
//
//    @Query(value = "SELECT p.serial_id FROM participant p JOIN room r ON p.room_id = r.serial_id WHERE r.room_short_code = :roomShortCode", nativeQuery = true)
//    List<Long> findParticipantsSerialIDsWith(@Param("roomShortCode") String roomShortCode);
//
//    @Query(value = "SELECT p.serial_id FROM participant p JOIN room r ON p.room_id = r.serial_id WHERE r.room_short_code = :roomShortCode AND p.is_organiser = FALSE", nativeQuery = true)
//    List<Long>  getPatientWithShortCode(@Param("roomShortCode") String roomShortCode);
//
//}
