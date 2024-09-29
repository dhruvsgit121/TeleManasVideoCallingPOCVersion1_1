package com.example.ehrc.telemanas.Service.NewStructuredService;

import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.CallStartDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.*;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Model.NewStructuredModal.EYDataModel.MHPDataModal;
import com.example.ehrc.telemanas.Model.NewStructuredModal.EYDataModel.PatientDataModal;
//import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import com.example.ehrc.telemanas.Model.NewStructuredModal.NewUserIdentityService;
import com.example.ehrc.telemanas.Service.NewServices.EventService;
import com.example.ehrc.telemanas.Service.NewServices.TwilioSMSService;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class RoomManagerService {

    @Autowired
    private AuthenticateUserFactory authenticateUserFactory;

    @Autowired
    private NewRoomService roomService;

    @Autowired
    private EventService eventService;

    @Autowired
    private NewUserIdentityService userIdentityService;

    @Autowired
    private TwilioSMSService twilioSMSService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

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

    public ResponseEntity<Map<String, Object>> deactivateRequestedRoom(String roomShortCode) {
        return roomService.deactivateRequestedRoom(roomShortCode);
    }

    public ResponseEntity<Map<String, Object>> getFile(String roomShortCode) {
        return userIdentityService.getFile(roomShortCode);
    }


    public ResponseEntity<Map<String, Object>> fetchUserIdentityUploadFlag(String roomShortCode) {
        return userIdentityService.fetchUserIdentityFlag(roomShortCode);
    }

    //Method to Join Video Call...
    public ResponseEntity<Map<String, Object>> JoinVideoCall(CallStartDTO callStartDTO) {

        System.out.println("CallStartDTO is : " + callStartDTO);

        ResponseEntity<Map<String, Object>> responseData = eventService.callStartSaveData(callStartDTO);
        if (responseData.getStatusCode() != HttpStatus.OK)
            return responseData;

        return roomService.joinRoom(callStartDTO);
    }

    public ResponseEntity<Map<String, Object>> leaveVideoCall(CallStartDTO callStartDTO) {

        System.out.println("leaveVideoCall : " + callStartDTO);

        ResponseEntity<Map<String, Object>> responseData = eventService.callEndSaveData(callStartDTO);
        if (responseData.getStatusCode() != HttpStatus.OK)
            return responseData;

        return roomService.MHPExitRoom(callStartDTO);
    }


    public ResponseEntity<Map<String, Object>> patientJoinCallFlagData(String roomShortCode) {
        return roomService.getPatientJoinData(roomShortCode);
    }

    //Method to Join Video Call...
    public ResponseEntity<Map<String, Object>> JoinPatientVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        return roomService.joinPatientRoom(roomDetailsRequest);
    }

    public ResponseEntity<Map<String, Object>> getPrescriptionDetails(String roomShortCode) {
        return prescriptionService.getPrescriptionDetails(roomShortCode);
    }

    public ResponseEntity<Map<String, Object>> uploadFile(MultipartFile file, String roomShortCode) {
        return userIdentityService.uploadFile(file, roomShortCode);
    }


    public ResponseEntity<Map<String, Object>> verifyUserIdentity(VerifyUserIdentityDTO verifyUserIdentityDTO) {
        return userIdentityService.verifyUserIdentity(verifyUserIdentityDTO);
    }

    public ResponseEntity<Map<String, Object>> getMHPRoomDetails(MHPRoomDetailsDTO mhpRoomDetailsDTO) {
        return roomService.getMHPRoomDetails(mhpRoomDetailsDTO);
    }


    public ResponseEntity<Map<String, Object>> getPatientRoomDetails(PatientRoomDetailsDTO patientRoomDetailsDTO) {


        return roomService.getPatientRoomDetails(patientRoomDetailsDTO);
//
//        if (videoCallingUtilities.getRoomActivationCheckResponseMap(patientRoomDetailsDTO.getRoomShortCode()) != null) {
//            return videoCallingUtilities.getRoomActivationCheckResponseMap(patientRoomDetailsDTO.getRoomShortCode());
//        }
//
//
//
//
//
//
//        return new ResponseEntity<>(videoCallingUtilities.getSuccessResponseMap(), HttpStatus.OK);

    }



    public ResponseEntity<Map<String, Object>> resendPrescriptionLink(AuthenticateUserDTO userDTOData, AuthenticationService authenticationService, SendPrescriptionDTO sendPrescriptionData) {
        return prescriptionService.resendPrescriptionLink(userDTOData, authenticationService, authenticateUserFactory, sendPrescriptionData);
//        System.out.println("resendPrescriptionLink called" + userDTOData);
//
//        System.out.println("resendPrescriptionLink called encryptedPhoneNumber" + sendPrescriptionData.getEncryptedPhoneNumber());
//
//        Map<String, Object> decryptPhoneNumberResponseMap = new HashMap<>();
//
//        ResponseEntity<Map<String, Object>> decryptUserPhoneNumberResponseMap = authenticationService.getDecryptedPhoneNumber(userDTOData, authenticateUserFactory,  sendPrescriptionData.getEncryptedPhoneNumber(), decryptPhoneNumberResponseMap);
//        if (decryptUserPhoneNumberResponseMap != null)
//            return decryptUserPhoneNumberResponseMap;
//
//        String decryptedPhoneNumber = "";
//
//        if (decryptPhoneNumberResponseMap != null && decryptPhoneNumberResponseMap.containsKey("decryptedPhoneNumber"))
//            decryptedPhoneNumber = decryptPhoneNumberResponseMap.get("decryptedPhoneNumber").toString();
//
//        System.out.println("resend prescription link phone number is : " + decryptedPhoneNumber);
//
//        twilioSMSService.sendPrescriptionLinkSMS(decryptedPhoneNumber, userDTOData.getRoomShortCode());
//
//        Map<String, Object> resposneMap = videoCallingUtilities.getSuccessResponseMap();
//
//        return new ResponseEntity<>(resposneMap, HttpStatus.OK);

    }


    public ResponseEntity<Map<String, Object>> resendVideoCallLink(AuthenticateUserDTO userDTOData, AuthenticationService authenticationService, String encryptedPhoneNumber) {

        System.out.println("resendVideoCallLink called" + userDTOData);

        System.out.println("resendVideoCallLink called encryptedPhoneNumber" + encryptedPhoneNumber);

        Map<String, Object> decryptPhoneNumberResponseMap = new HashMap<>();

        ResponseEntity<Map<String, Object>> decryptUserPhoneNumberResponseMap = authenticationService.getDecryptedPhoneNumber(userDTOData, authenticateUserFactory, encryptedPhoneNumber, decryptPhoneNumberResponseMap);
        if (decryptUserPhoneNumberResponseMap != null)
            return decryptUserPhoneNumberResponseMap;

        String decryptedPhoneNumber = "";

        if (decryptPhoneNumberResponseMap != null && decryptPhoneNumberResponseMap.containsKey("decryptedPhoneNumber"))
            decryptedPhoneNumber = decryptPhoneNumberResponseMap.get("decryptedPhoneNumber").toString();

        System.out.println("reswnd link phone number is : " + decryptedPhoneNumber);

        twilioSMSService.sendVideoCallLinkSMS(decryptedPhoneNumber, userDTOData.getRoomShortCode());
        Map<String, Object> resposneMap = videoCallingUtilities.getSuccessResponseMap();

        return new ResponseEntity<>(resposneMap, HttpStatus.OK);

    }
}