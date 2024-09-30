package com.example.ehrc.telemanas.Service.NewStructuredService;


import com.example.ehrc.telemanas.DTO.VideoCallEventsDTO;
import com.example.ehrc.telemanas.Model.NewStructuredModal.*;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationEventMasterRepository;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationEventRepository;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationRoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class NewEventService {

    @Autowired
    private VideoConsultationEventMasterRepository videoConsultationEventMasterRepository;

    @Autowired
    private VideoConsultationRoomRepository videoConsultationRoomRepository;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private VideoConsultationEventRepository videoConsultationEventRepository;

    public VideoConsultationEventMaster getVideoConsultationMasterRecord(String eventDescription) {

        VideoConsultationEventMaster masterRecord = videoConsultationEventMasterRepository.findEventMasterWithDescription(eventDescription.toLowerCase());

        if (masterRecord == null) {
            VideoConsultationEventMaster newMasterRecord = new VideoConsultationEventMaster(eventDescription.toLowerCase(), true, videoCallingUtilities.getDateTimeWithOffset(0), null);
            videoConsultationEventMasterRepository.save(newMasterRecord);
            return newMasterRecord;
        }

        return masterRecord;
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> saveVideoCallEvent(VideoCallEventsDTO videoCallEventsDTO) {

        Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();

        if(videoCallingUtilities.getRoomActivationCheckResponseMap(videoCallEventsDTO.getRoomShortCode()) != null)
            return videoCallingUtilities.getRoomActivationCheckResponseMap(videoCallEventsDTO.getRoomShortCode());

        VideoConsultationRoom requestedRoom  = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(videoCallEventsDTO.getRoomShortCode());

        VideoConsultationEvent videoConsultationEvent = new VideoConsultationEvent(videoCallingUtilities.getDateTimeWithOffset(0));

        videoConsultationEvent.setVideoCall(requestedRoom.getVideoCallData());

        videoConsultationEvent.setParticipant(getParticipant(videoCallEventsDTO.getIsMHP(), requestedRoom.getParticipants()));

        VideoConsultationEventMaster videoConsultationEventMaster = getVideoConsultationMasterRecord(videoCallEventsDTO.getEventDescription().toLowerCase());
        videoConsultationEvent.setVideoCallEvent(videoConsultationEventMaster);

        videoConsultationEventRepository.save(videoConsultationEvent);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }



    public VideoConsultationParticipant getParticipant(int isMHP, List<VideoConsultationParticipant> participantsList) {

      boolean isMHPUser = (isMHP == 1);

      for(VideoConsultationParticipant participant : participantsList){
          if(isMHPUser && participant.isOrganiser()){
              return participant;
          } else if (!isMHPUser && !participant.isOrganiser()) {
              return participant;
          }
      }
      return null;
    }
}
