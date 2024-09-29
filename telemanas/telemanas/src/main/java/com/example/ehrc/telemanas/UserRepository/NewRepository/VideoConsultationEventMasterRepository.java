package com.example.ehrc.telemanas.UserRepository.NewRepository;

import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationEventMaster;
import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoConsultationEventMasterRepository extends JpaRepository<VideoConsultationEventMaster, Long> {



    @Query(value = "SELECT * FROM video_consultation_event_master WHERE name = :eventDescription", nativeQuery = true)
    VideoConsultationEventMaster findEventMasterWithDescription(@Param("eventDescription") String eventDescription);




}



