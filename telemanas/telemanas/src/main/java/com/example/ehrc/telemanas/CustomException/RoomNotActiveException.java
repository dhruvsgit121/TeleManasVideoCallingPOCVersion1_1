package com.example.ehrc.telemanas.CustomException;

public class RoomNotActiveException extends RuntimeException{

    public RoomNotActiveException(String message) {
        super(message);
    }

}
