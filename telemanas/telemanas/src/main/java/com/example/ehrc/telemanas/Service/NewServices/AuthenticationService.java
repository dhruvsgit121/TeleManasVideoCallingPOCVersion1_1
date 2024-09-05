package com.example.ehrc.telemanas.Service.NewServices;


import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationService {

    public ResponseEntity<Map<String, Object>> authenticateMHPData(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory) {

        //Checking for Authentication of MHP...
        ResponseEntity<Map<String, Object>> MHPResponseData = authenticateUserFactory.authenticateUser("mhp", userDTOData);
        return MHPResponseData;
    }

    public ResponseEntity<Map<String, Object>> authenticatePatientData(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory) {

        //Checking for Authentication of Patient Data...
        ResponseEntity<Map<String, Object>> PatientResponseData = authenticateUserFactory.authenticateUser("patient", userDTOData);
        return PatientResponseData;
    }

}
