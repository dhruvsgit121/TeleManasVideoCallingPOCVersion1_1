package com.example.ehrc.telemanas.AuthenticateService;

import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.GlobalRequestHandler.AutheticateUserRequestHandler.AuthenticateUserRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class AuthenticateMHP implements AuthenticateUser{

    @Autowired
    private AuthenticateUserRequestHandler authenticateUserRequestHandler;

    @Override
    public ResponseEntity<Map<String, Object>> authenticateUser(AuthenticateUserDTO userData) {

        if (authenticateUserRequestHandler == null) {
            authenticateUserRequestHandler = new AuthenticateUserRequestHandler();
        }

        return authenticateUserRequestHandler.autheticateMHPData(userData);
    }
}
