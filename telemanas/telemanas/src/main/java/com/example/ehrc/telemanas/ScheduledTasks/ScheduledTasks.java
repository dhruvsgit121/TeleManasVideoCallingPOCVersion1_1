package com.example.ehrc.telemanas.ScheduledTasks;


import com.example.ehrc.telemanas.Model.Room;
import com.example.ehrc.telemanas.Service.RoomService;
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

    @Autowired
    private RoomService roomService;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    //Time set for 10 seconds
    @Scheduled(fixedRate = 60 * 1000)
    public void invalidateExpiredRooms() {

        LocalDateTime expirationDate = videoCallingUtilities.getDateTimeWithOffset(roomJWTValidityOffSet);
        List<Room> roomList = roomService.getRoomListWithExpirationdate(expirationDate);

        System.out.println("Number of rooms deactivated from scheduler are : " + roomList.size());

        if (roomList.size() > 0) {
            for (Room room : roomList) {
                room.setActive(false);
                roomService.saveRoom(room);
            }
        }
    }
}
