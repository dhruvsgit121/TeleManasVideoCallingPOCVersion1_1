package com.example.ehrc.telemanas.ControllerAdvice;


import com.example.ehrc.telemanas.CustomException.RoomDoesNotExistException;
import com.example.ehrc.telemanas.CustomException.RoomNotActiveException;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class RoomExceptionControllerAdvice {

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @ExceptionHandler(RoomDoesNotExistException.class)
    public ResponseEntity<Map<String, Object>> handleRoomNotFoundException(RoomDoesNotExistException ex) {
        return videoCallingUtilities.getErrorResponseMessageEntity("Room you requested didn't exists.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoomNotActiveException.class)
    public ResponseEntity<Map<String, Object>> handleRoomNotActiveException(RoomDoesNotExistException ex) {
        return videoCallingUtilities.getErrorResponseMessageEntity("Room you requested is not active. Please try again.", HttpStatus.BAD_REQUEST);
    }

}
