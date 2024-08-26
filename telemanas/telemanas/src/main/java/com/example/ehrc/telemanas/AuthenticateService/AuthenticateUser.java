package com.example.ehrc.telemanas.AuthenticateService;

import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public interface AuthenticateUser {

    public ResponseEntity<Map<String, Object>> authenticateUser(AuthenticateUserDTO userData);

}
