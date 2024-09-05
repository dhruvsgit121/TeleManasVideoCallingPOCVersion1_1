package com.example.ehrc.telemanas.UserRepository;

//import com.example.ehrc.telemanas.Model.Participant;
//import com.example.ehrc.telemanas.Model.Room;
//import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedAuthenticatedUser;
import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UpdatedRoomRepository extends JpaRepository<UpdatedRoom, Long> {


//    SELECT *
//    FROM public.updated_participant p
//    JOIN public.updated_room r ON p.room_id = r.id
//    JOIN public.updated_autheticated_user u ON p.user_id = u.user_id WHERE r.is_active = TRUE

    @Query(value = "SELECT DISTINCT r.* AS participant_id FROM updated_participant p JOIN updated_room r ON p.room_id = r.id JOIN updated_authenticated_user u ON p.user_id = u.user_id WHERE r.is_active = TRUE", nativeQuery = true)
    List<UpdatedRoom> findAllActiveRoomLists();

    @Query(value = "SELECT * FROM updated_room WHERE room_short_code = :roomShortCode", nativeQuery = true)
    UpdatedRoom findRoomDetailsWith(@Param("roomShortCode") String roomShortCode);


    @Query(value = "SELECT * FROM updated_room WHERE expiration_date < :expirationDate AND is_active = TRUE", nativeQuery = true)
    List<UpdatedRoom> findRoomListWithExpirationDate(@Param("expirationDate") LocalDateTime expirationDate);

}
