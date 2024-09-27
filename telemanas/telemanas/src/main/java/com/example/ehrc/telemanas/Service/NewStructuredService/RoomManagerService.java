package com.example.ehrc.telemanas.Service.NewStructuredService;

import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.Model.NewStructuredModal.EYDataModel.MHPDataModal;
import com.example.ehrc.telemanas.Model.NewStructuredModal.EYDataModel.PatientDataModal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RoomManagerService {

    @Autowired
    private AuthenticateUserFactory authenticateUserFactory;

    @Autowired
    private NewRoomService roomService;

    public ResponseEntity<Map<String, Object>> createMeetingLink(AuthenticateUserDTO userAuthorisationDataDTO, AuthenticationService authenticationService) {

        ResponseEntity<Map<String, Object>> userDataAuthenticationResponseMap = authenticationService.authenticateMHPAndPatientData(userAuthorisationDataDTO, authenticateUserFactory);
        if (userDataAuthenticationResponseMap.getStatusCode() != HttpStatus.OK)
            return userDataAuthenticationResponseMap;

        MHPDataModal mhpUserData = null;
        PatientDataModal patientUserData = null;

        if (userDataAuthenticationResponseMap.hasBody() && userDataAuthenticationResponseMap.getBody().containsKey("patientData")) {
            patientUserData = new PatientDataModal((Map<String, Object>) userDataAuthenticationResponseMap.getBody().get("patientData"));
        }

        if (userDataAuthenticationResponseMap.hasBody() && userDataAuthenticationResponseMap.getBody().containsKey("mhpData")) {
            mhpUserData = new MHPDataModal((Map<String, Object>) userDataAuthenticationResponseMap.getBody().get("mhpData"));
        }

        Map<String, Object> decryptPhoneNumberResponseMap = new HashMap<>();

        ResponseEntity<Map<String, Object>> decryptUserPhoneNumberResponseMap = authenticationService.getDecryptedPhoneNumber(userAuthorisationDataDTO, authenticateUserFactory, patientUserData.getEncryptedMobileNumber(), decryptPhoneNumberResponseMap);
        if (decryptUserPhoneNumberResponseMap != null)
            return decryptUserPhoneNumberResponseMap;

        if (decryptPhoneNumberResponseMap != null && decryptPhoneNumberResponseMap.containsKey("decryptedPhoneNumber"))
            patientUserData.setMobileNumber(decryptPhoneNumberResponseMap.get("decryptedPhoneNumber").toString());

        return processNewRoomCreation(userAuthorisationDataDTO, mhpUserData, patientUserData);
    }
    
    public ResponseEntity<Map<String, Object>> processNewRoomCreation(AuthenticateUserDTO userAuthorisationDataDTO, MHPDataModal mhpUserData, PatientDataModal patientUserData) {
        return roomService.createRoom(userAuthorisationDataDTO, patientUserData, mhpUserData);
    }
}