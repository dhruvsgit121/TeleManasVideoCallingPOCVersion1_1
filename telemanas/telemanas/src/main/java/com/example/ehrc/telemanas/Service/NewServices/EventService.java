package com.example.ehrc.telemanas.Service.NewServices;

import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.CallStartDTO;
import com.example.ehrc.telemanas.DTO.VideoCallEventsDTO;
import com.example.ehrc.telemanas.GlobalRequestHandler.CallEventDataRequestHandler;
import com.example.ehrc.telemanas.Model.UpdatedModels.Event;
import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import com.example.ehrc.telemanas.Service.RoomService;
import com.example.ehrc.telemanas.UserRepository.EventRepository;
import com.example.ehrc.telemanas.UserRepository.RoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EventService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private CallEventDataRequestHandler callEventDataRequestHandler;


    //VideoCallEventsDTO videoCallEventsDTO

    public ResponseEntity<Map<String, Object>> saveEventData(String roomShortCode, String eventDescription) {

        Map<String, Object> response = new HashMap<>();

        Room room = roomService.getUpdatedActiveRoomDetailsWith(roomShortCode);

        System.out.println("Rom data is : " + room);

        if (room == null)
            return roomService.getRoomValidationResponseEntity();

        Event eventData = new Event(videoCallingUtilities.getDateTimeWithOffset(0), eventDescription);
        eventRepository.save(eventData);
        room.addEvent(eventData);

        roomRepository.save(room);

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();

        responseData.put("Event Id", eventData.getEventId());
        responseData.put("Event Time Stamp", eventData.getEventTimeStamp());
        responseData.put("Event Description", eventData.getEventDescription());
        responseData.put("Room ID", eventData.getRoom().getRoomId());

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

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

        Room room = roomService.getUpdatedActiveRoomDetailsWith(callStartDTO.getRoomShortCode());

        if (room == null)
            return roomService.getRoomValidationResponseEntity();

        return isStartCall ?
                callEventDataRequestHandler.saveCallStartData(callStartDTO, room.getVideoId()) :
                callEventDataRequestHandler.saveCallEndData(callStartDTO, room.getVideoId(), videoCallingUtilities.getDateTimeWithOffset(0));
    }
}
