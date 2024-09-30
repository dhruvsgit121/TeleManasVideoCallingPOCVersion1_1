package com.example.ehrc.telemanas.Service.NewServices;

import com.example.ehrc.telemanas.DTO.CallStartDTO;
import com.example.ehrc.telemanas.GlobalRequestHandler.CallEventDataRequestHandler;
import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationRoom;
//import com.example.ehrc.telemanas.Service.NewStructuredService.NewRoomService;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationRoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import java.util.HashMap;
import java.util.Map;

@Service
public class EventService {

    @Autowired
    private VideoConsultationRoomRepository videoConsultationRoomRepository;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private CallEventDataRequestHandler callEventDataRequestHandler;

//    @Autowired
//    private NewRoomService newRoomService;

//    public ResponseEntity<Map<String, Object>> saveEventData(String roomShortCode, String eventDescription) {
//
//        Map<String, Object> response = new HashMap<>();
//
//        Room room = roomService.getUpdatedActiveRoomDetailsWith(roomShortCode);
//
//        System.out.println("Rom data is : " + room);
//
//        if (room == null)
//            return roomService.getRoomValidationResponseEntity();
//
//        Event eventData = new Event(videoCallingUtilities.getDateTimeWithOffset(0), eventDescription);
//        eventRepository.save(eventData);
//        room.addEvent(eventData);
//
//        roomRepository.save(room);
//
//        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();
//
//        responseData.put("Event Id", eventData.getEventId());
//        responseData.put("Event Time Stamp", eventData.getEventTimeStamp());
//        responseData.put("Event Description", eventData.getEventDescription());
//        responseData.put("Room ID", eventData.getRoom().getRoomId());
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    //Method to Send Call Start Event...
    public ResponseEntity<Map<String, Object>> callStartSaveData(CallStartDTO callStartDTO) {
        return callEventSaveData(callStartDTO, true);
    }

    //Method to Send Call End Event...
    public ResponseEntity<Map<String, Object>> callEndSaveData(CallStartDTO callStartDTO) {
        return callEventSaveData(callStartDTO, false);
    }

    //Method to Send Call Event...
    public ResponseEntity<Map<String, Object>> callEventSaveData(CallStartDTO callStartDTO, boolean isStartCall) {

        VideoConsultationRoom room = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(callStartDTO.getRoomShortCode());

        System.out.println("callEventSaveData called in EventService.java");

        if(videoCallingUtilities.getRoomActivationCheckResponseMap(callStartDTO.getRoomShortCode()) != null) {
//            System.out.println("Entered in tbis loop");
//
//            System.out.println("Response is : " + videoCallingUtilities.getRoomActivationCheckResponseMap(callStartDTO.getRoomShortCode()) );
            return videoCallingUtilities.getRoomActivationCheckResponseMap(callStartDTO.getRoomShortCode());
        }

        System.out.println("callEventSaveData video id is : " + room.getVideoCallData().getVideoCallId());

        return isStartCall ?
                callEventDataRequestHandler.saveCallStartData(callStartDTO, room.getVideoCallData().getVideoCallId()) :
                callEventDataRequestHandler.saveCallEndData(callStartDTO, room.getVideoCallData().getVideoCallId(), videoCallingUtilities.getDateTimeWithOffset(0));
    }
}
