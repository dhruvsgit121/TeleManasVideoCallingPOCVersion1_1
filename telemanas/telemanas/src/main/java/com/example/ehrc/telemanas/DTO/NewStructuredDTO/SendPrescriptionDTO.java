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

}