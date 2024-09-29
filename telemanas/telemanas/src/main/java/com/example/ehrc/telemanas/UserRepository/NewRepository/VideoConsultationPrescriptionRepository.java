package com.example.ehrc.telemanas.UserRepository.NewRepository;

import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationPrescription;
import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface VideoConsultationPrescriptionRepository extends JpaRepository<VideoConsultationPrescription, Long> {

    @Query(value = "SELECT * FROM video_consultation_prescription WHERE video_consultation_call_id = :callID", nativeQuery = true)
    VideoConsultationPrescription findPrescriptionDetailsWith(@Param("callID") Long callID);

}
