package com.example.ehrc.telemanas.ScheduledTasks;


import com.example.ehrc.telemanas.Model.Room;
import com.example.ehrc.telemanas.Service.RoomService;
import com.example.ehrc.telemanas.UserRepository.RoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

    @Value("${jwt.RoomJWTValidityOffSet}")
    private Long roomJWTValidityOffSet;


//    @Value("${jwt.SchedulerTimeDuration}")
//    private Long roomSchedulerDuration;

//    jwt.SchedulerTimeDuration

    @Autowired
    private RoomService roomService;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;


    @Scheduled(fixedRate = 10 * 1000)
    public void runTaskEveryFiveSeconds() {

        System.out.println("Task executed every " + roomJWTValidityOffSet + " seconds");

        LocalDateTime expirationDate = videoCallingUtilities.getDateTimeWithOffset(roomJWTValidityOffSet);
        System.out.println(expirationDate);

        List<Room> roomList = roomService.getRoomListWithExpirationdate(expirationDate);

        if (roomList.size() > 0) {
            for (Room room : roomList) {
                room.setActive(false);
                roomService.saveRoom(room);
            }
        }
    }
}
