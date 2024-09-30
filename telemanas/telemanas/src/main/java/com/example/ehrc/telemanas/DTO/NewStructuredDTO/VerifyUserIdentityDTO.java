package com.example.ehrc.telemanas.DTO.NewStructuredDTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class VerifyUserIdentityDTO {

    @NotNull(message = "Room Short Code cannot be null. Please enter Room Short Code to proceed.")
    @NotBlank(message = "Room Short Code cannot be blank. Please enter Room Short Code to proceed.")
    private String roomShortCode;

    private int isVerified;

}
