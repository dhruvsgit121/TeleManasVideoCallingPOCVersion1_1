package com.example.ehrc.telemanas.UserRepository.NewRepository;

import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationRoom;
import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoConsultationRoomRepository extends JpaRepository<VideoConsultationRoom, Long> {


    @Query(value = "SELECT DISTINCT r.* AS participant_id FROM video_consultation_participant p JOIN video_consultation_room r ON p.room_id = r.id WHERE r.is_active = TRUE", nativeQuery = true)
    List<VideoConsultationRoom> findAllActiveRoomLists();

    @Query(value = "SELECT * FROM video_consultation_room WHERE room_short_code = :roomShortCode AND is_active = TRUE", nativeQuery = true)
    VideoConsultationRoom findRoomDetailsWithActiveStatus(@Param("roomShortCode") String roomShortCode);

    @Query(value = "SELECT * FROM video_consultation_room WHERE room_short_code = :roomShortCode", nativeQuery = true)
    VideoConsultationRoom findRoomDetailsWith(@Param("roomShortCode") String roomShortCode);


}
