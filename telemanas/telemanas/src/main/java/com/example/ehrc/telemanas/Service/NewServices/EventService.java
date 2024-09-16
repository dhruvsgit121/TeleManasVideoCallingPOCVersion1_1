package com.example.ehrc.telemanas.Service.NewServices;

import com.example.ehrc.telemanas.DTO.VideoCallEventsDTO;
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

    public ResponseEntity<Map<String, Object>> saveEventData(VideoCallEventsDTO videoCallEventsDTO) {

        Map<String, Object> response = new HashMap<>();

        Room room = roomService.getUpdatedActiveRoomDetailsWith(videoCallEventsDTO.getRoomShortCode());

        System.out.println("Rom data is : " + room);

        if (room == null)
            return roomService.getRoomValidationResponseEntity();

        Event eventData = new Event(videoCallingUtilities.getDateTimeWithOffset(0), videoCallEventsDTO.getEventDescription());
        eventRepository.save(eventData);
        room.addEvent(eventData);

        roomRepository.save(room);

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();

        responseData.put("Event Id", eventData.getEventId());
        responseData.put("Event Time Stamp", eventData.getEventTimeStamp());
        responseData.put("Event Description", eventData.getEventDescription());
        responseData.put("Room ID", eventData.getRoom().getRoomId());

        return new ResponseEntity<>(responseData, HttpStatus.OK) ;
    }
}
