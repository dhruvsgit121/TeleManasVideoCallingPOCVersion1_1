package com.example.ehrc.telemanas.Service.NewStructuredService;

import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationRoom;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationParticipantRepository;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationRoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VideoConsultationCallService {

    public enum VideoCallStatus {
        STARTED,
        INPROGRESS,
        PATIENTJOINED,
        ENDED
    }

    @Autowired
    private VideoConsultationParticipantRepository videoConsultationParticipantRepository;

    @Autowired
    private VideoConsultationRoomRepository videoConsultationRoomRepository;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Transactional
    public void SetVideoCallStatus(String roomShortCode, VideoConsultationCallService.VideoCallStatus callStatus) {
        VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(roomShortCode);
        requestedRoom.getVideoCallData().setCallStatus(callStatus);
        videoConsultationRoomRepository.save(requestedRoom);
    }

    @Transactional
    public void SetVideoCallJoiningTime(String roomShortCode) {
        VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(roomShortCode);
        //requestedRoom.getVideoCallData().setCallStatus(callStatus);
        requestedRoom.getVideoCallData().setCallStartTime(videoCallingUtilities.getDateTimeWithOffset(0));
        videoConsultationRoomRepository.save(requestedRoom);
    }


    public void SetVideoCallStatusForUser(String roomShortCode, boolean isMHP) {
        SetVideoCallStatus(roomShortCode, isMHP ? VideoConsultationCallService.VideoCallStatus.INPROGRESS : VideoConsultationCallService.VideoCallStatus.PATIENTJOINED);
    }

}
