package com.example.ehrc.telemanas.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateUserDTO {

    @Override
    public String toString() {
        return "AuthenticateUserDTO{" +
                "telemanasId='" + telemanasId + '\'' +
                ", userUuid='" + userUuid + '\'' +
                ", bearerToken='" + bearerToken + '\'' +
                ", loggedInId='" + loggedInId + '\'' +
                '}';
    }

    @NotNull(message = "Telemanas ID cannot be null. Please enter Telemanas ID to proceed.")
    @NotBlank(message = "Telemanas ID cannot be blank. Please enter Telemanas ID to proceed.")
    private String telemanasId;

    @NotNull(message = "UUID cannot be null. Please enter UUID to proceed.")
    @NotBlank(message = "UUID cannot be blank. Please enter UUID to proceed.")
    private String userUuid;

    @NotNull(message = "MHP Username cannot be null. Please enter MHP Username to proceed.")
    @NotBlank(message = "MHP Username cannot be blank. Please enter MHP Username to proceed.")
    private String mhpUserName;

    private String bearerToken;

    private String loggedInId;

    private String roomShortCode;
}
