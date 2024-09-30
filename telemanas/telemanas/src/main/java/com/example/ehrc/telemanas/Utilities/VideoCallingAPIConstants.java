package com.example.ehrc.telemanas.Utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VideoCallingAPIConstants {

    public static String isErrorFlagValue= "isErrorPresent";
    public static String errorMessageValue= "errorMessage";

    public static String ERROR_MESSAGE_ROOM_DOES_NOT_EXISTS = "Room you requested is either expired or does not exists.";
    public static String ERROR_MESSAGE_PRESCRIPTION_ROOM_DOES_NOT_EXISTS = "Room you requested does not exists.";
    public static String ERROR_MESSAGE_PRESCRIPTION_DOES_NOT_EXISTS = "Prescription does not exists for the requested room.";


    public static String getUserDetailsURL;
    public static String decryptPatientMobileNumberURL;
    public static String callStartEventURL;
    public static String callEndEventURL;
    public static String authenticatePatientURL;
    public static String eyAPIBaseURL;
    public static String getPatientPrescriptionURL;


    public VideoCallingAPIConstants(@Value("${videocalling.api.autheticate.patient}") String authenticatePatientURL,
                               @Value("${videocalling.api.getuserdetails.username}") String getUserDetailsURL,
                               @Value("${videocalling.api.decrypt.mobilenumber}") String decryptPatientMobileNumberURL,
                                    @Value("${videocalling.api.eybase.url}") String eyAPIBaseURL,
                                    @Value("${videocalling.api.call.start.event}") String callStartEventURL,
                                    @Value("${videocalling.api.eybase.url}") String callEndEventURL,
                                    @Value("${videocalling.api.get_prescription}") String getPrescriptionURL
                                    ) {
        this.authenticatePatientURL = authenticatePatientURL;
        this.getUserDetailsURL = getUserDetailsURL;
        this.decryptPatientMobileNumberURL = decryptPatientMobileNumberURL;
        this.eyAPIBaseURL = eyAPIBaseURL;
        this.callStartEventURL = callStartEventURL;
        this.callEndEventURL = callEndEventURL;
        this.getPatientPrescriptionURL =getPrescriptionURL;
    }
}
