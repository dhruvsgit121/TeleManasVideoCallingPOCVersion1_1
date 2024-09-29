package com.example.ehrc.telemanas.DTO.NewStructuredDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString

public class SendPrescriptionDTO {

    @NotNull(message = "IVRS Call ID cannot be null. Please enter IVRS Call ID to proceed.")
    @NotBlank(message = "IVRS Call ID cannot be blank. Please enter IVRS Call ID to proceed.")
    private String ivrsCallID;

    private String encryptedPhoneNumber;

    private String bearerToken;

    private String loggedInId;

    private String roomShortCode;

//
//    @NotNull(message = "User UUID cannot be null. Please enter User UUID to proceed.")
//    @NotBlank(message = "User UUID cannot be blank. Please enter User UUID to proceed.")
//    private String userUUID;
//
//    @NotNull(message = "Room Short Code cannot be null. Please enter Room Short Code to proceed.")
//    @NotBlank(message = "Room Short Code cannot be blank. Please enter Room Short Code to proceed.")
//    private String roomShortCode;

//    private int isMHP;
//
//    private int isErrorWhileCallEnd;
//
//    private String bearerToken;
//
//    private String loggedInId;

}