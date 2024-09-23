package com.example.ehrc.telemanas.Service;


import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.PrescriptionDTO;
import com.example.ehrc.telemanas.DTO.RequestPrescriptionDTO;
import com.example.ehrc.telemanas.DTO.SendPrescriptionSMS;
import com.example.ehrc.telemanas.Model.UpdatedModels.Prescription;
import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import com.example.ehrc.telemanas.Service.NewServices.TwilioSMSService;
import com.example.ehrc.telemanas.Service.NewServices.UpdatedRoomService;
import com.example.ehrc.telemanas.Service.NewServices.VideoCallService;
import com.example.ehrc.telemanas.UserRepository.PrescriptionRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private RoomService roomService;

    @Autowired
    private VideoCallService videoCallService;

    @Autowired
    private UpdatedRoomService updatedRoomService;

    @Autowired
    private TwilioSMSService twilioSMSService;

    public ResponseEntity<Map<String, Object>> sendPrescriptionData(AuthenticateUserDTO userDTOData, AuthenticateUserFactory authenticateUserFactory, String roomShortCode) {

        Room roomData = updatedRoomService.findRoomDetailsWith(roomShortCode);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put(VideoCallingAPIConstants.isErrorFlagValue, false);

        String encryptedPhoneNumber = VideoCallService.getEncryptedPhoneNumber(roomData);

        //Decrypting Mobile Number of The Patient...
        ResponseEntity<Map<String, Object>> decryptMobileData = authenticateUserFactory.decryptUserPhoneNumber(userDTOData, encryptedPhoneNumber);
        if (decryptMobileData.getStatusCode() != HttpStatus.OK)
            return decryptMobileData;

        System.out.println("Data Recieved is in mobile decrypt is : " + decryptMobileData.getBody());

        String errorMessage = "Some error Occurred. Please try again later.";

        if (decryptMobileData.hasBody()) {
            if (decryptMobileData.getBody().containsKey("responsePhoneNo")) {
                String mobileNumber = decryptMobileData.getBody().get("responsePhoneNo").toString();
                sendLinkToPatient(mobileNumber, roomShortCode);
                return new ResponseEntity<>(responseData, HttpStatus.OK);
            }
            if (decryptMobileData.getBody().containsKey("message") && decryptMobileData.getBody().get("message") != null) {
                errorMessage = decryptMobileData.getBody().get("message").toString();
            }
        }
        responseData.put(VideoCallingAPIConstants.errorMessageValue, errorMessage);
        return new ResponseEntity<>(responseData, HttpStatus.SEE_OTHER);
    }

    private void sendLinkToPatient(String patientNumber, String roomShortCode) {
        String registeredMobileNumber = "+91" + patientNumber;
        //To be commented out to send SMS to the Patient...

//        sendNimhansSMS(registeredMobileNumber, roomShortCode);
        twilioSMSService.sendPrescriptionSms(registeredMobileNumber, roomShortCode);
    }


    public ResponseEntity<Map<String, Object>> savePrescriptionData(PrescriptionDTO prescriptionDTO) {
        Prescription prescription = new Prescription(prescriptionDTO);
        System.out.println("prescription data is : " + prescription.getAge());
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        Map<String, Object> response = videoCallingUtilities.getSuccessResponseMap();
        response.put("id", savedPrescription.getPrescriptionID());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> getPrescriptionData(RequestPrescriptionDTO requestPrescriptionDTO) {
        Prescription prescription = prescriptionRepository.findprecriptionData(requestPrescriptionDTO.getRoomShortCode());
        Map<String, Object> response = videoCallingUtilities.getSuccessResponseMap();
        response.put("prescriptionData", prescription);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
