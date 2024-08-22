package com.example.ehrc.telemanas.AuthenticateService;

import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.GlobalRequestHandler.AutheticateUserRequestHandler.AuthenticateUserRequestHandler;
import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticatePatient implements AuthenticateUser {

    @Autowired
    private AuthenticateUserRequestHandler authenticateUserRequestHandler;

    @Override
    public ResponseEntity<Map<String, Object>> authenticateUser(AuthenticateUserDTO userData) {

        if (authenticateUserRequestHandler == null) {
            authenticateUserRequestHandler = new AuthenticateUserRequestHandler();
        }

        return authenticateUserRequestHandler.autheticateUserData(userData);
    }

    public ResponseEntity<Map<String, Object>> decryptMobileNumber(AuthenticateUserDTO userData, String encryptedMobileNumber) {

        if (authenticateUserRequestHandler == null) {
            authenticateUserRequestHandler = new AuthenticateUserRequestHandler();
        }

        return authenticateUserRequestHandler.decryptPatientMobileNumber(userData, encryptedMobileNumber);
    }


}
