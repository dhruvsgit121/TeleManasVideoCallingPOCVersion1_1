package com.example.ehrc.telemanas.DTO.NewStructuredDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendVideoCallLinkDTO {

    private String encryptedPhoneNumber;

    private String bearerToken;

    private String loggedInId;

    private String roomShortCode;

}
