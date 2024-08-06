package com.example.ehrc.telemanas.DTO;

public class CreateRoomDTO {

    private Long mhpID;

    private Long patientId;

    public Long getMhpID() {
        return mhpID;
    }

    public void setMhpID(Long mhpID) {
        this.mhpID = mhpID;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}

