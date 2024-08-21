package com.example.ehrc.telemanas.AuthenticateService;

import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticateUserFactory {

    public ResponseEntity<Map<String, Object>> authenticateUser(String userType, AuthenticateUserDTO userData) {

        AuthenticateUser user;
        user = new AuthenticatePatient();
        return user.authenticateUser(userData);
    }

}
