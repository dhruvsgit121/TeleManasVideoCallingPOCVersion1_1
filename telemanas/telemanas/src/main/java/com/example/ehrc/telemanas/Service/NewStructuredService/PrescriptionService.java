package com.example.ehrc.telemanas.Service.NewStructuredService;


import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.SendPrescriptionDTO;
//import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationCall;
import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationPrescription;
import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationRoom;
import com.example.ehrc.telemanas.Service.NewServices.TwilioSMSService;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationPrescriptionRepository;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationRoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
//import org.hibernate.tool.schema.spi.SchemaTruncator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PrescriptionService {

    @Autowired
    private TwilioSMSService twilioSMSService;

    @Autowired
    private VideoConsultationRoomRepository videoConsultationRoomRepository;

    @Autowired
    private VideoConsultationPrescriptionRepository videoConsultationPrescriptionRepository;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    public ResponseEntity<Map<String, Object>> resendPrescriptionLink(AuthenticateUserDTO userDTOData, AuthenticationService authenticationService, AuthenticateUserFactory authenticateUserFactory, SendPrescriptionDTO sendPrescriptionData) {

        Map<String, Object> decryptPhoneNumberResponseMap = new HashMap<>();

        if(videoCallingUtilities.getRoomActivationCheckResponseMap(sendPrescriptionData.getRoomShortCode()) != null)
            return videoCallingUtilities.getRoomActivationCheckResponseMap(sendPrescriptionData.getRoomShortCode());

        ResponseEntity<Map<String, Object>> decryptUserPhoneNumberResponseMap = authenticationService.getDecryptedPhoneNumber(userDTOData, authenticateUserFactory, sendPrescriptionData.getEncryptedPhoneNumber(), decryptPhoneNumberResponseMap);
        if (decryptUserPhoneNumberResponseMap != null)
            return decryptUserPhoneNumberResponseMap;

        String decryptedPhoneNumber = "";

        if (decryptPhoneNumberResponseMap != null && decryptPhoneNumberResponseMap.containsKey("decryptedPhoneNumber"))
            decryptedPhoneNumber = decryptPhoneNumberResponseMap.get("decryptedPhoneNumber").toString();

        System.out.println("resend prescription link phone number is : " + decryptedPhoneNumber);

        twilioSMSService.sendPrescriptionLinkSMS(decryptedPhoneNumber, userDTOData.getRoomShortCode());

        sendPrescriptionLinkDataData(sendPrescriptionData);

        Map<String, Object> resposneMap = videoCallingUtilities.getSuccessResponseMap();
        return new ResponseEntity<>(resposneMap, HttpStatus.OK);
    }

    public void sendPrescriptionLinkDataData(SendPrescriptionDTO sendPrescriptionData){
        VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(sendPrescriptionData.getRoomShortCode());
        savePrescriptionLinVideoCallPrescriptionData(requestedRoom, sendPrescriptionData);
    }

    public void savePrescriptionLinVideoCallPrescriptionData(VideoConsultationRoom requestedRoom, SendPrescriptionDTO sendPrescriptionData){
        VideoConsultationPrescription prescription = videoConsultationPrescriptionRepository.findPrescriptionDetailsWith(requestedRoom.getVideoCallData().getId());
        if(prescription == null){
            VideoConsultationPrescription prescriptionData = new VideoConsultationPrescription(videoCallingUtilities.getDateTimeWithOffset(0), sendPrescriptionData.getIvrsCallID());
            prescriptionData.setVideoCallData(requestedRoom.getVideoCallData());
            videoConsultationPrescriptionRepository.save(prescriptionData);
        }
    }
}
