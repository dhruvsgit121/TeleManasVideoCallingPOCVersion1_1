package com.example.ehrc.telemanas.Service.NewStructuredService;


import org.springframework.stereotype.Service;

@Service
public class VideoConsultationCallService {

    public enum VideoCallStatus {
        STARTED,
        INPROGRESS,
        PATIENTJOINED,
        ENDED
    }

}
