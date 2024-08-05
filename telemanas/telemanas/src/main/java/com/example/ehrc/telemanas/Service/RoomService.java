package com.example.ehrc.telemanas.Service;


import com.example.ehrc.telemanas.Model.Room;
import com.example.ehrc.telemanas.Model.User;
import com.example.ehrc.telemanas.UserRepository.RoomRepository;
import com.example.ehrc.telemanas.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Room saveRoom(Room room){
        return roomRepository.save(room);
    }

//    public List<User> saveAllUsers(List<User> user) throws SQLException {
//        return userRepository.saveAll(user);
//    }
}
