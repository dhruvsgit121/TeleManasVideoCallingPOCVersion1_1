package com.example.ehrc.telemanas.UserRepository;

import com.example.ehrc.telemanas.Model.UpdatedModels.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long>{

    @Query(value = "SELECT * FROM prescription WHERE room_code = :roomShortCode", nativeQuery = true)
    Prescription findprecriptionData(@Param("roomShortCode") String roomShortCode);

}
