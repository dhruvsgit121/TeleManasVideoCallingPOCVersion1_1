package com.example.ehrc.telemanas.Model.EYDataModel;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class MHPDataModal {

    private String mhpName;
//    private String age;
//    private String telemanasId;
//    private String encryptedMobileNumber;
//    private String mobileNumber;

    @Override
    public String toString() {
        return "MHPDataModal{" +
                "mhpName='" + mhpName + '\'' +
                '}';
    }


//    @Override
//    public String toString() {
//        return "EYUserDataModal{" +
//                "patientName='" + patientName + '\'' +
//                ", age='" + age + '\'' +
//                ", telemanasId='" + telemanasId + '\'' +
//                ", encryptedMobileNumber='" + encryptedMobileNumber + '\'' +
//                ", mobileNumber='" + mobileNumber + '\'' +
//                '}';
//    }
    //    @Override
//    public String toString() {
//        return "EYUserDataModal{" +
//                "patientName='" + patientName + '\'' +
//                ", age='" + age + '\'' +
//                ", telemanasId='" + telemanasId + '\'' +
//                '}';
//    }

    public MHPDataModal(Map<String, Object> EYUserData) {

        System.out.println("Enetered here in this loop");

        if (EYUserData.containsKey("name"))
            this.mhpName = EYUserData.get("name").toString();

//        if (EYUserData.containsKey("age"))
//            this.age = EYUserData.get("age").toString();
//
//        if (EYUserData.containsKey("telemanasId"))
//            this.telemanasId = EYUserData.get("telemanasId").toString();
//
//        if (EYUserData.containsKey("phone"))
//            this.encryptedMobileNumber = EYUserData.get("phone").toString();
//
////        if (EYUserData.containsKey("telemanasId"))
//            this.telemanasId = EYUserData.get("telemanasId").toString();

    }

//    userId=181,
//    userName=rohan.raj,
//    roleId=3,
//    roleName=psych_sw,
//    roleDispName=Psychiatric Social Worker,
//    telemanasOrgRoleId=138,
//    telemanasOrgRoleName=NIMHANS,
//    userUuid=a59b752f-a7a4-4b34-a89d-0f5c6b6b15d0,
//    menu={},
//    componentName=,
//    name=rohan.raj,
//    priveledgeList=[],
//    restrictedOrg=false
}
