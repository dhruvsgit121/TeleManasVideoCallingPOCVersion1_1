package com.example.ehrc.telemanas.CustomException;

public class RoomDoesNotExistException extends RuntimeException{

    public RoomDoesNotExistException(String message) {
        super(message);
    }

}
