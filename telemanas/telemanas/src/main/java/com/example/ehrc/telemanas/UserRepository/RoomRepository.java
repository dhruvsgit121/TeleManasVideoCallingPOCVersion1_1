package com.example.ehrc.telemanas.UserRepository;

import com.example.ehrc.telemanas.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "SELECT * FROM room WHERE room_short_code = :roomShortCode", nativeQuery = true)
    Room findRoomDetailsWith(@Param("roomShortCode") String roomShortCode);

}
