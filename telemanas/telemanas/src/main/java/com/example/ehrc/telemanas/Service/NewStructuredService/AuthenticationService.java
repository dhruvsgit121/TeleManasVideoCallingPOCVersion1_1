package com.example.ehrc.telemanas.Service.NewStructuredService;


import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    public ResponseEntity<Map<String, Object>> authenticateMHPAndPatientData(AuthenticateUserDTO userAuthorisationDataDTO, AuthenticateUserFactory authenticateUserFactory) {

        Map<String, Object> responseMap = new HashMap<>();

        ResponseEntity<Map<String, Object>> patientResponseData = authenticateAndParsePatientData(userAuthorisationDataDTO, authenticateUserFactory, responseMap);
        if (patientResponseData != null && patientResponseData.getStatusCode() != HttpStatus.OK)
            return patientResponseData;

        ResponseEntity<Map<String, Object>> mhpResponseData = authenticateAndParseMHPData(userAuthorisationDataDTO, authenticateUserFactory, responseMap);
        if (mhpResponseData != null && mhpResponseData.getStatusCode() != HttpStatus.OK)
            return mhpResponseData;

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }


    public ResponseEntity<Map<String, Object>> authenticateAndParseMHPData(AuthenticateUserDTO userAuthorisationDataDTO, AuthenticateUserFactory authenticateUserFactory, Map<String, Object> responseMap) {

        //MHP Authentication Data...
        ResponseEntity<Map<String, Object>> MHPAuthenticationResponseData = authenticateMHPData(userAuthorisationDataDTO, authenticateUserFactory);

        System.out.println("createMeetingLink response in RoomManagerService MHPAuthenticationResponseData : " + MHPAuthenticationResponseData);

        if (MHPAuthenticationResponseData.getStatusCode() != HttpStatus.OK)
            return MHPAuthenticationResponseData;

        if (MHPAuthenticationResponseData.hasBody())
            responseMap.put("mhpData", MHPAuthenticationResponseData.getBody());

        return null;
    }

    public ResponseEntity<Map<String, Object>> authenticateAndParsePatientData(AuthenticateUserDTO userAuthorisationDataDTO, AuthenticateUserFactory authenticateUserFactory, Map<String, Object> responseMap) {

        ResponseEntity<Map<String, Object>> patientAuthenticationResponseData = authenticatePatientData(userAuthorisationDataDTO, authenticateUserFactory);
        System.out.println("createMeetingLink response in RoomManagerService patientAuthenticationResponseData : " + patientAuthenticationResponseData);

        if (patientAuthenticationResponseData.getStatusCode() != HttpStatus.OK)
            return patientAuthenticationResponseData;

        if (patientAuthenticationResponseData.hasBody())
            responseMap.put("patientData", patientAuthenticationResponseData.getBody());

        return null;
    }


    public ResponseEntity<Map<String, Object>> getDecryptedPhoneNumber(AuthenticateUserDTO userAuthorisationDataDTO, AuthenticateUserFactory authenticateUserFactory, String encryptedPhoneNumber, Map<String, Object> responseMap) {

        ResponseEntity<Map<String, Object>> patientMobileDecryptionResponseData = authenticateUserFactory.decryptUserPhoneNumber(userAuthorisationDataDTO, encryptedPhoneNumber);

        if (patientMobileDecryptionResponseData.getStatusCode() != HttpStatus.OK)
            return patientMobileDecryptionResponseData;

        if (patientMobileDecryptionResponseData.hasBody())
            responseMap.put("decryptedPhoneNumber", patientMobileDecryptionResponseData.getBody().get("responsePhoneNo"));

        return null;
    }

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
