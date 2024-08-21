package com.example.ehrc.telemanas.ControllerAdvice;


import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class ValidationMessagesExceptionHandler {

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @ExceptionHandler(ValidationMessagesException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ValidationMessagesException ex) {
        return videoCallingUtilities.getErrorResponseMessageEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//
////            System.out.println;
//
//            errors.put(fieldName, errorMessage);
//        });
//
//
//        System.out.println("error dict is : " + errors);
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }

}
