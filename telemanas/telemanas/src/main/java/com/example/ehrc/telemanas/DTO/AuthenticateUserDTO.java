package com.example.ehrc.telemanas.DTO;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

//import javax.validation.constraints.NEWotNull;

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
    private String telemanasId;

    @NotNull(message = "UUID cannot be null. Please enter UUID to proceed.")
    private String userUuid;

    private String bearerToken;

    private String loggedInId;
}
