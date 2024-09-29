package com.example.ehrc.telemanas.DTO.NewStructuredDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRoomDetailsDTO {

//    private String bearerToken;
//
//    private String loggedInId;

    private String roomShortCode;

    private int isFirstConsentProvided;

    private int isSecondConsentProvided;

}
