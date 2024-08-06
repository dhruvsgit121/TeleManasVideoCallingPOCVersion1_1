package com.example.ehrc.telemanas.CustomException;

public class ValidationMessagesException extends RuntimeException{

    public ValidationMessagesException(String message) {
        super(message);
    }
}
