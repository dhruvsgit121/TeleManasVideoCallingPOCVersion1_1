package com.example.ehrc.telemanas.AuthenticateService;

import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.GlobalRequestHandler.AutheticateUserRequestHandler.AuthenticateUserRequestHandler;
//import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticatePatient implements AuthenticateUser {

    private final AuthenticateUserRequestHandler authenticateUserRequestHandler = new AuthenticateUserRequestHandler();

    @Override
    public ResponseEntity<Map<String, Object>> authenticateUser(AuthenticateUserDTO userData) {
        return authenticateUserRequestHandler.authenticateUserData(userData);
    }

    public ResponseEntity<Map<String, Object>> decryptMobileNumber(AuthenticateUserDTO userData, String encryptedMobileNumber) {
        return authenticateUserRequestHandler.decryptPatientMobileNumber(userData, encryptedMobileNumber);
    }

    public ResponseEntity<Map<String, Object>> getPrescriptionData(AuthenticateUserDTO userData, String ivrsCallID) {
        return authenticateUserRequestHandler.getPrescriptionData(userData, ivrsCallID);
    }
}
