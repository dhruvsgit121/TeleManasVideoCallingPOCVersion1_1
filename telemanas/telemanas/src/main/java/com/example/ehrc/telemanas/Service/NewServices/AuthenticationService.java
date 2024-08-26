package com.example.ehrc.telemanas.Service.NewServices;


import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationService {

    public ResponseEntity<Map<String, Object>> autheticateParticipantsData(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory) {

        //Checking for Authentication of Patient Data...
        ResponseEntity<Map<String, Object>> PatientResponseData = authenticateUserFactory.authenticateUser("patient", userDTOData);
        if (PatientResponseData.getStatusCode() != HttpStatus.OK)
            return PatientResponseData;

        //Checking for Authentication of MHP...
        ResponseEntity<Map<String, Object>> MHPResponseData = authenticateUserFactory.authenticateUser("mhp", userDTOData);
        if (MHPResponseData.getStatusCode() != HttpStatus.OK)
            return MHPResponseData;

        return PatientResponseData;
    }

}
