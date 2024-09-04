package com.example.ehrc.telemanas.Service.NewServices;


import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedRoom;
//import com.example.ehrc.telemanas.Service.ParticipantService;
import com.example.ehrc.telemanas.UserRepository.UpdatedRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UpdatedRoomService {

    @Autowired
    private UpdatedRoomRepository updatedRoomRepository;

    public UpdatedRoom findRoomDetailsWith(String roomShortCode) {
        return updatedRoomRepository.findRoomDetailsWith(roomShortCode);
    }
}
