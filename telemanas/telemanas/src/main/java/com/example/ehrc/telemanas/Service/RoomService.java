package com.example.ehrc.telemanas.Service;


import com.example.ehrc.telemanas.Model.Room;
import com.example.ehrc.telemanas.UserRepository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Room saveRoom(Room room){
        return roomRepository.save(room);
    }

    public Room getRoomDetailsWith(String shortCode){
        return roomRepository.findRoomDetailsWith(shortCode);
    }

    public List<Room> getRoomListWithExpirationdate(LocalDateTime expirationDate){
        return roomRepository.findRoomListWithExpirationDate(expirationDate);
    }
}
