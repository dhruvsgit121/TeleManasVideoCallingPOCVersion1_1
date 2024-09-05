package com.example.ehrc.telemanas.DTO;

import com.example.ehrc.telemanas.Model.UpdatedModels.AuthenticatedUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor

public class RoomCreationUserDTO {

    private String participantID;
    private String userName;
    private String jwtToken;
    private Boolean isOrganiser;

    private AuthenticatedUser.UserRole userRole;

}
