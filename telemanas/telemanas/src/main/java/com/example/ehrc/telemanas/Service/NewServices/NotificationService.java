package com.example.ehrc.telemanas.Service.NewServices;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    private final String authToken;
    private final String accountSid;
    private final String twilioPhoneNumber;

    @Value("${sms.patientMessageURL}")
    private String patientMeetingLinkBaseURL;

    public NotificationService(@Value("${twilio.auth.token}") String authToken,
                               @Value("${twilio.account.sid}") String accountSid,
                               @Value("${twilio.phone.number}") String twilioPhoneNumber) {
        this.authToken = authToken;
        this.accountSid = accountSid;
        this.twilioPhoneNumber = twilioPhoneNumber;

        System.out.println("Account SID: " + accountSid);
        System.out.println("Auth Token: " + authToken);
        System.out.println("Sender phone number : " + twilioPhoneNumber);

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

    public void sendTestSms(String toPhoneNumber, String textBody) {

        String messageTextBody = patientMeetingLinkBaseURL + textBody;

        System.out.println("message send to : " + toPhoneNumber + " with text : " + messageTextBody);

        try {
            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    messageTextBody
            ).create();
            System.out.println("SMS Send to the user...");
        } catch (Exception exception) {
            System.out.println(exception.getLocalizedMessage());
        }
    }
}
