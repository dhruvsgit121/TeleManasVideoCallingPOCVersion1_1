package com.example.ehrc.telemanas.UserRepository.NewRepository;


//import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationRoom;
import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoConsultationUserRepository extends JpaRepository<VideoConsultationUser, Long> {

    @Query(value = "SELECT * FROM video_consultation_user WHERE userid = :userID", nativeQuery = true)
    VideoConsultationUser findUswerWithUserID(@Param("userID") String userID);

}
