package com.example.ehrc.telemanas.Utilities;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
//import java.time.OffsetDateTime;
//import java.time.ZoneOffset;

import java.time.Instant;
import java.util.Date;

@Service
public class VideoCallingUtilities {


    public static long getExpirationTimeStamp(int timeStampOffset) {
        return Instant.now().plusSeconds(timeStampOffset).getEpochSecond();
    }

    public static long getCurrentTimeStamp() {
        return Instant.now().getEpochSecond();
    }

    public String generateRandomString(int stringLength) {
        boolean useLetters = true;
        boolean useNumbers = true;
        String generatedString = RandomStringUtils.random(stringLength, useLetters, useNumbers);
        return generatedString;
    }

    public LocalDateTime getDateTimeWithOffset(long offSet){
        LocalDateTime now = LocalDateTime.now().plusSeconds(offSet);
        return now;
    }
}
