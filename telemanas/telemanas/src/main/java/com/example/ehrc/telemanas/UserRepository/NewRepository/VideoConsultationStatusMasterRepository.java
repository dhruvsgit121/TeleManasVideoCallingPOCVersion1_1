package com.example.ehrc.telemanas.UserRepository.NewRepository;

import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationStatusMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoConsultationStatusMasterRepository extends JpaRepository<VideoConsultationStatusMaster, Long> {

    @Query(value = "SELECT * FROM video_consultation_status_master WHERE name = :statusName", nativeQuery = true)
    VideoConsultationStatusMaster findStatusWithStatusName(@Param("statusName") String statusName);

}
