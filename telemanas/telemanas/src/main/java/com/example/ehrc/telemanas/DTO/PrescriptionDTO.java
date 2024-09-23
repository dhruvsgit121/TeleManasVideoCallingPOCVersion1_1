package com.example.ehrc.telemanas.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionDTO {

    private String patientName;
    private String age;

    private String telemanasID;
    private String gender;
    private String date;

    private String pharmaIntervention;
    private String roomCode;
    private String mhpName;
    private String registrationID;
    private String tmcName;
}
