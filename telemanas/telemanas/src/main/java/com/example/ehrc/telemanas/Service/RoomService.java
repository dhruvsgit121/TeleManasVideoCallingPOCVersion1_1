package com.example.ehrc.telemanas.Service;


import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.Room;
import com.example.ehrc.telemanas.UserRepository.RoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;


    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room getRoomDetailsWith(String shortCode) {
        return roomRepository.findRoomDetailsWith(shortCode);
    }

    public List<Room> getRoomListWithExpirationdate(LocalDateTime expirationDate) {
        return roomRepository.findRoomListWithExpirationDate(expirationDate);
    }


    public Map<String, Object> generateVideoRoomDetailsResponseEntity(RoomDetailsRequestDTO roomDetailsRequest) {

        Room roomDetails = this.getRoomDetailsWith(roomDetailsRequest.getRoomShortCode());

        if (roomDetails == null || !roomDetails.isActive() || roomDetails.getParticipants().size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        ArrayList<Participant> participantsList = new ArrayList<>(roomDetails.getParticipants());

        Map<String, Object> responseData = new HashMap<>();

        Participant firstParticipant = participantsList.get(0);
        Participant secondParticipant = participantsList.get(1);

        System.out.println("first participant data is :" + firstParticipant.getParticipantId());
        System.out.println("Second participant data is :" + secondParticipant.getParticipantId());

        responseData.put("roomID", roomDetails.getRoomId());

        if ((roomDetailsRequest.getIsMHP() == 1 && firstParticipant.getUserRole().equals(Participant.UserRole.MHP)) || (roomDetailsRequest.getIsMHP() != 1 && firstParticipant.getUserRole().equals(Participant.UserRole.PATIENT))) {
            responseData.put("jwtToken", firstParticipant.getJwtToken());
            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), firstParticipant.getJwtToken()));
        } else {
            responseData.put("jwtToken", secondParticipant.getJwtToken());
            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), secondParticipant.getJwtToken()));
        }
//
        if (roomDetailsRequest.getIsMHP() != 1) {
            String clientID = firstParticipant.getUserRole().equals(Participant.UserRole.MHP) ? firstParticipant.getParticipantId() : secondParticipant.getParticipantId();
            responseData.put("clientID", clientID);

//            System.out.println("entered in the loop!!!");
//            //User is a patient, who is joining the call...
//            String clientID = firstParticipant.getUserRole().equals(Participant.UserRole.MHP) ? firstParticipant.getParticipantId() : secondParticipant.getParticipantId();
//            sseService.sendCustomMessage(clientID + "", "Hello patient joined the call...");
        }
//
        System.out.println("Response data from the /getroomdetails API is : " + responseData);

        return responseData;
    }
}
