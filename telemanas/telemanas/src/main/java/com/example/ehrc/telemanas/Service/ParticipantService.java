//package com.example.ehrc.telemanas.Service;
//
////import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
////import com.example.ehrc.telemanas.Model.Participant;
////import com.example.ehrc.telemanas.UserRepository.ParticipantRepository;
////import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
////import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
////import java.time.LocalDateTime;
////import java.util.HashMap;
////import java.util.List;
////import java.util.Map;
//
//@Service
//public class ParticipantService {
//
////    @Autowired
////    private ParticipantRepository participantRepository;
//
////    public Participant getParticipantByID(Long participantID) {
////        return participantRepository.getReferenceById(participantID);
////    }
//
////    public List<Long> getPatientParticipant(String roomShortCode) {
////        return participantRepository.getPatientWithShortCode(roomShortCode);
////    }
//
////    public ResponseEntity<Map<String, Object>> getPatientJoinData(String roomShortCode) {
////
////        Map<String, Object> responseData = new HashMap<>();
////        responseData.put(VideoCallingAPIConstants.isErrorFlagValue, false);
////
////        List<Long> participantIds = participantRepository.findParticipantsSerialIDsWith(roomShortCode);
////
////        for (Long participantID : participantIds) {
////            Participant participant = participantRepository.getReferenceById(participantID);
////            if (!participant.isOrganiser() && participant.isHasJoinedRoom()) {
////                responseData.put("joinedRoom", participant.isHasJoinedRoom());
////                return new ResponseEntity(responseData, HttpStatus.OK);
////            }
////        }
////        return new ResponseEntity(responseData, HttpStatus.OK);
////    }
//
//
////    public Participant saveParticipant(Participant participant) {
////        return participantRepository.save(participant);
////    }
////
//
////    public List<String> getRoomShortCodeWith(String MHPId, String patientID, LocalDateTime expirationDate) {
////        return participantRepository.findRoomShortCodeWith(MHPId, patientID, expirationDate);
////    }
////
////    public List<Long> getParticipantsListWith(String roomCode) {
////        return participantRepository.findParticipantsSerialIDsWith(roomCode);
////    }
//}
