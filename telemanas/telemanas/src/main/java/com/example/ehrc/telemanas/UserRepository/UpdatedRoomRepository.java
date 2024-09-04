package com.example.ehrc.telemanas.UserRepository;

//import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedAuthenticatedUser;
import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UpdatedRoomRepository extends JpaRepository<UpdatedRoom, Long> {


//    SELECT *
//    FROM public.updated_participant p
//    JOIN public.updated_room r ON p.room_id = r.id
//    JOIN public.updated_autheticated_user u ON p.user_id = u.user_id WHERE r.is_active = TRUE

    @Query(value = "SELECT DISTINCT r.* AS participant_id FROM updated_participant p JOIN updated_room r ON p.room_id = r.id JOIN updated_authenticated_user u ON p.user_id = u.user_id WHERE r.is_active = TRUE", nativeQuery = true)
    List<UpdatedRoom> findAllActiveRoomLists();

}
