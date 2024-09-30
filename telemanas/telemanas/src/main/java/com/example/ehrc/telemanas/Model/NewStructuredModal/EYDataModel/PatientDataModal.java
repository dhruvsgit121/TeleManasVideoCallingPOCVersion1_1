package com.example.ehrc.telemanas.Model.NewStructuredModal.EYDataModel;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PatientDataModal {

    private String patientName;
    private String age;
    private String telemanasId;
    private String encryptedMobileNumber;
    private String mobileNumber;

    @Override
    public String toString() {
        return "EYUserDataModal{" +
                "patientName='" + patientName + '\'' +
                ", age='" + age + '\'' +
                ", telemanasId='" + telemanasId + '\'' +
                ", encryptedMobileNumber='" + encryptedMobileNumber + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }

    public PatientDataModal(Map<String, Object> EYUserData) {

        System.out.println("Enetered here in this loop");

        if (EYUserData.containsKey("patientName"))
            this.patientName = EYUserData.get("patientName").toString();

        if (EYUserData.containsKey("age"))
            this.age = EYUserData.get("age").toString();

        if (EYUserData.containsKey("telemanasId"))
            this.telemanasId = EYUserData.get("telemanasId").toString();

        if (EYUserData.containsKey("phone"))
            this.encryptedMobileNumber = EYUserData.get("phone").toString();

    }
}
