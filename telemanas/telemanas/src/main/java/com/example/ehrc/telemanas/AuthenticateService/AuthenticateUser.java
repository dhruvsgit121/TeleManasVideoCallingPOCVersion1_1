package com.example.ehrc.telemanas.AuthenticateService;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthenticateUser {

    public ResponseEntity<Map<String, Object>> authenticateUser();

}
