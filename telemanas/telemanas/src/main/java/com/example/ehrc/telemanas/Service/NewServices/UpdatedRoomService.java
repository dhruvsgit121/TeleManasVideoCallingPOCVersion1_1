package com.example.ehrc.telemanas.Service.NewServices;


//import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedAuthenticatedUser;
import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedParticipant;
import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
//import com.example.ehrc.telemanas.Service.ParticipantService;
import com.example.ehrc.telemanas.UserRepository.UpdatedRoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service

public class UpdatedRoomService {

    @Autowired
    private UpdatedRoomRepository updatedRoomRepository;

    public Room findRoomDetailsWith(String roomShortCode) {
        return updatedRoomRepository.findRoomDetailsWith(roomShortCode);
    }


    public ResponseEntity<Map<String, Object>> getPatientJoinData(String roomShortCode) {

        Map<String, Object> responseData = new HashMap<>();
        responseData.put(VideoCallingAPIConstants.isErrorFlagValue, false);

        Room roomData = updatedRoomRepository.findRoomDetailsWith(roomShortCode);

        if (roomData.getParticipants().size() == 2) {
//            UpdatedParticipant firstParticipant = roomData.getParticipants().get(0);
//            UpdatedParticipant secondParticipant = roomData.getParticipants().get(1);

            UpdatedParticipant patientParticipantData = roomData.getParticipants().get(0).getAuthenticatedUser().getUserRole().equals(UpdatedAuthenticatedUser.UserRole.PATIENT) ? roomData.getParticipants().get(0) : roomData.getParticipants().get(1);

            if (patientParticipantData.isHasJoinedRoom()) {
                responseData.put("joinedRoom", patientParticipantData.isHasJoinedRoom());
            }
        }
        return new ResponseEntity(responseData, HttpStatus.OK);
    }
}
