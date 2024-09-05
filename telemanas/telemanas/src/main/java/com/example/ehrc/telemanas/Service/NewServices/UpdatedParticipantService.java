package com.example.ehrc.telemanas.Service.NewServices;


//import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
//import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedRoom;
import com.example.ehrc.telemanas.UserRepository.ParticipantRepository;
//import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

@Service
public class UpdatedParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void saveUpdatedParticipantData(Participant participant) {
        participantRepository.save(participant);
    }


//    public ResponseEntity<Map<String, Object>> getPatientJoinData(String roomShortCode) {
//
//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put(VideoCallingAPIConstants.isErrorFlagValue, false);
//
//        UpdatedRoom roomData =
//
//
////        List<Long> participantIds = participantRepository.findParticipantsSerialIDsWith(roomShortCode);
////
////        for (Long participantID : participantIds) {
////            Participant participant = participantRepository.getReferenceById(participantID);
////            if (!participant.isOrganiser() && participant.isHasJoinedRoom()) {
////                responseData.put("joinedRoom", participant.isHasJoinedRoom());
////                return new ResponseEntity(responseData, HttpStatus.OK);
////            }
////        }
//        return new ResponseEntity(responseData, HttpStatus.OK);
//    }


}
