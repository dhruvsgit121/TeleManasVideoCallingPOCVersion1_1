package com.example.ehrc.telemanas.Service.NewStructuredService;

//import com.example.ehrc.telemanas.DTO.CallStartDTO;

//import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationParticipant;
import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationRoom;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationParticipantRepository;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import java.util.Map;

@Service
public class VideoConsultationParticipantService {

//    @Autowired
//    private VideoConsultationParticipantRepository videoConsultationParticipantRepository;
//
//    @Autowired
//    private VideoConsultationRoomRepository videoConsultationRoomRepository;
//
//    @Transactional
//    public void SetVideoCallStatus(String roomShortCode, boolean isMHP) {
//
//        VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(roomShortCode);
//
//        requestedRoom.getVideoCallData().setCallStatus(isMHP ? VideoConsultationCallService.VideoCallStatus.INPROGRESS : VideoConsultationCallService.VideoCallStatus.PATIENTJOINED);
//        videoConsultationRoomRepository.save(requestedRoom);
//
//    }
}
