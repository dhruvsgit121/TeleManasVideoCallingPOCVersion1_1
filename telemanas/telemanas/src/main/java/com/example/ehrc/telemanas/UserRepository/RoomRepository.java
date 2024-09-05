package com.example.ehrc.telemanas.UserRepository;

import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "SELECT DISTINCT r.* AS participant_id FROM participant p JOIN room r ON p.room_id = r.id JOIN authenticated_user u ON p.user_id = u.user_id WHERE r.is_active = TRUE", nativeQuery = true)
    List<Room> findAllActiveRoomLists();

    @Query(value = "SELECT * FROM room WHERE room_short_code = :roomShortCode", nativeQuery = true)
    Room findRoomDetailsWith(@Param("roomShortCode") String roomShortCode);


    @Query(value = "SELECT * FROM room WHERE expiration_date < :expirationDate AND is_active = TRUE", nativeQuery = true)
    List<Room> findRoomListWithExpirationDate(@Param("expirationDate") LocalDateTime expirationDate);

}
