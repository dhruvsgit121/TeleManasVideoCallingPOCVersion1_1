package com.example.ehrc.telemanas.Model.EYDataModel;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class MHPDataModal {

    private String mhpName;
    private String roleDisplayName;

    @Override
    public String toString() {
        return "MHPDataModal{" +
                "mhpName='" + mhpName + '\'' +
                ", roleDisplayName='" + roleDisplayName + '\'' +
                '}';
    }

    //    @Override
//    public String toString() {
//        return "MHPDataModal{" +
//                "mhpName='" + mhpName + '\'' +
//                '}';
//    }

    public MHPDataModal(Map<String, Object> EYUserData) {

        System.out.println("Enetered here in this loop" + EYUserData);

        if (EYUserData.containsKey("name"))
            this.mhpName = EYUserData.get("name").toString();

        if (EYUserData.containsKey("name"))
            this.roleDisplayName = EYUserData.get("roleDispName").toString();

    }
}
