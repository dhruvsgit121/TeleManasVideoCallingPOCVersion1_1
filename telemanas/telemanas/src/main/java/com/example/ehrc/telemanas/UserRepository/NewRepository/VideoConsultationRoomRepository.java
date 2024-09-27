package com.example.ehrc.telemanas.UserRepository.NewRepository;

import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoConsultationRoomRepository extends JpaRepository<VideoConsultationRoom, Long> {


    @Query(value = "SELECT DISTINCT r.* AS participant_id FROM video_consultation_participant p JOIN video_consultation_room r ON p.room_id = r.id WHERE r.is_active = TRUE", nativeQuery = true)
    List<VideoConsultationRoom> findAllActiveRoomLists();



}
