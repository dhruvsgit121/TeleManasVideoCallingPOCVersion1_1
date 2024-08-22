package com.example.ehrc.telemanas.Model.EYDataModel;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EYUserDataModal {

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
    //    @Override
//    public String toString() {
//        return "EYUserDataModal{" +
//                "patientName='" + patientName + '\'' +
//                ", age='" + age + '\'' +
//                ", telemanasId='" + telemanasId + '\'' +
//                '}';
//    }

    public EYUserDataModal(Map<String, Object> EYUserData) {

        System.out.println("Enetered here in this loop");

        if (EYUserData.containsKey("patientName"))
            this.patientName = EYUserData.get("patientName").toString();

        if (EYUserData.containsKey("age"))
            this.age = EYUserData.get("age").toString();

        if (EYUserData.containsKey("telemanasId"))
            this.telemanasId = EYUserData.get("telemanasId").toString();

        if (EYUserData.containsKey("phone"))
            this.encryptedMobileNumber = EYUserData.get("phone").toString();

//        if (EYUserData.containsKey("telemanasId"))
//            this.telemanasId = EYUserData.get("telemanasId").toString();

    }
}


 /* {patientName=Shivangi AFMS,
                age=82,
                gender=Male,
                state=Confidential (Armed Forces),
                district=Confidential (Armed Forces),
                recordAddTime=,
                phone=AQIDBAUGBwgJCgsMjCyXQS84t33Wlldy/FrLLUurBB5ah7kLGOw=,
                city=Confidential (Armed Forces),
                taluka=, individualCall=,
                telemanasId=829858441784,
            preferName=false,
                preferAge=false,
                region=Urban,
                callerName=afms on
            behalf test,
                    callerDetails=On behalf of someone,
                relationship=4,
                relationshipName=Spouse,
                relationshipDetails=,
                stateId=-1,
                genderId=1,
                districtId=-1,
                individualCalling=,
                flag=true,
                workingAt=,
                typeOfHCW=,
                describeTypeOfHCW=,
                typeChildHCW=,
                typeOfHCWName=,
                typeChildHCWName=,
                preferDistrict=false,
                preferState=false,
                merits=null,
                callerNameDisp=afms on behalf test,
                restrict=true}
        */
