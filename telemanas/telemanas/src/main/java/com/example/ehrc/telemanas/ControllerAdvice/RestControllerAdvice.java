package com.example.ehrc.telemanas.ControllerAdvice;

import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
//import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;


@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        //Iterating the Validation Errors We got...
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });


        //Response Data Map...
        Map<String, Object> responseData = new HashMap<>();
        responseData.put(VideoCallingAPIConstants.isErrorFlagValue, true);

        LinkedHashSet<String> errorCodeKeysSet = new LinkedHashSet<>(errors.keySet());

        String errorMessage = "";

        for(String errorKey : errorCodeKeysSet){
            errorMessage = errors.get(errorKey);
            break;
        }
        return videoCallingUtilities.getErrorResponseMessageEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingRequestHeaderValidationExceptions(MissingRequestHeaderException ex) {

        System.out.println("Exception called at : " + ex.getMessage()) ;
        System.out.println("Exception called at : " + ex.getLocalizedMessage()) ;

        return videoCallingUtilities.getErrorResponseMessageEntity(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }
}
