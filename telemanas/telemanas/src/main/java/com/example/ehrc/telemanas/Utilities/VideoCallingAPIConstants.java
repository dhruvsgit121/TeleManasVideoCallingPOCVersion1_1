package com.example.ehrc.telemanas.Utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VideoCallingAPIConstants {

    public static String isErrorFlagValue= "isErrorPresent";
    public static String errorMessageValue= "errorMessage";

    public static String getUserDetailsURL;
    public static String decryptPatientMobileNumberURL;
    public static String authenticatePatientURL;
    public static String eyAPIBaseURL;

    public VideoCallingAPIConstants(@Value("${videocalling.api.autheticate.patient}") String authenticatePatientURL,
                               @Value("${videocalling.api.getuserdetails.username}") String getUserDetailsURL,
                               @Value("${videocalling.api.decrypt.mobilenumber}") String decryptPatientMobileNumberURL,
                                    @Value("${videocalling.api.eybase.url}") String eyAPIBaseURL) {
        this.authenticatePatientURL = authenticatePatientURL;
        this.getUserDetailsURL = getUserDetailsURL;
        this.decryptPatientMobileNumberURL = decryptPatientMobileNumberURL;
        this.eyAPIBaseURL = eyAPIBaseURL;
    }
}
