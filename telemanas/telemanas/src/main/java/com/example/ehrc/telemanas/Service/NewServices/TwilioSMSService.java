package com.example.ehrc.telemanas.Service.NewServices;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class TwilioSMSService {

    private final String authToken;
    private final String accountSid;
    private final String twilioPhoneNumber;

    @Value("${sms.patientMessageURL}")
    private String patientMeetingLinkBaseURL;

    @Value("${prescriptionLink.patientMessageURL}")
    private String patientPrescriptonLinkBaseURL;

    public TwilioSMSService(@Value("${twilio.auth.token}") String authToken,
                            @Value("${twilio.account.sid}") String accountSid,
                            @Value("${twilio.phone.number}") String twilioPhoneNumber) {
        this.authToken = authToken;
        this.accountSid = accountSid;
        this.twilioPhoneNumber = twilioPhoneNumber;

        if (accountSid == null || authToken == null || twilioPhoneNumber == null) {
            throw new IllegalStateException("Twilio credentials are not configured properly");
        }

        try {
            Twilio.init(this.accountSid, this.authToken);
        } catch (ApiException e) {
            System.err.println("Twilio API Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendVideoCallLinkSMS(String toPhoneNumber, String textBody) {
        String messageTextBody = patientMeetingLinkBaseURL + textBody;
        sendTestSms(toPhoneNumber, messageTextBody);
    }

    public void sendPrescriptionLinkSMS(String toPhoneNumber, String textBody) {
        String messageTextBody = patientPrescriptonLinkBaseURL + textBody;
        sendTestSms(toPhoneNumber, messageTextBody);
    }

    public void sendTestSms(String toPhoneNumber, String textBody) {

        System.out.println("message send to : " + toPhoneNumber + " with text : " + textBody);

        try {
            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    textBody
            ).create();
            System.out.println("SMS Send to the user...");
        } catch (Exception exception) {
            System.out.println(exception.getLocalizedMessage());
            System.out.println("Eneterded in catch block");
            System.out.println(exception);
        }
    }
}
