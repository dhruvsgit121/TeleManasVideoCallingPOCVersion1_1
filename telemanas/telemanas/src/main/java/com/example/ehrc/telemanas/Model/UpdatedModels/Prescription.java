package com.example.ehrc.telemanas.Model.UpdatedModels;

import com.example.ehrc.telemanas.DTO.PrescriptionDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor


@Entity
public class Prescription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long prescriptionID;

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

    public Prescription(PrescriptionDTO prescriptionDTO){
        this.patientName = prescriptionDTO.getPatientName();
        this.age = prescriptionDTO.getAge();

        this.telemanasID = prescriptionDTO.getTelemanasID();
        this.gender = prescriptionDTO.getGender();
        this.date = prescriptionDTO.getDate();

        this.pharmaIntervention = prescriptionDTO.getPharmaIntervention();
        this.roomCode = prescriptionDTO.getRoomCode();
        this.mhpName = prescriptionDTO.getMhpName();
        this.registrationID = prescriptionDTO.getRegistrationID();
        this.tmcName = prescriptionDTO.getTmcName();

    }
}

