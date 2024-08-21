package com.example.ehrc.telemanas.AuthenticateService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticateUserFactory {

    public ResponseEntity<Map<String, Object>> authenticateUser(String userType){

        AuthenticateUser user;

//        if(user.equals("patient")){
        user = new AutheticatePatient();

//        }

        return user.authenticateUser();
    }

}
