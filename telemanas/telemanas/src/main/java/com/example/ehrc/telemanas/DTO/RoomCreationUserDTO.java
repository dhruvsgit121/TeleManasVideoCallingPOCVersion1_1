package com.example.ehrc.telemanas.DTO;

//import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.UpdatedModels.AuthenticatedUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor

public class RoomCreationUserDTO {

    //Participant patientUser = new Participant(null, null, patientJWTToken, userDTOData.getTelemanasId(), false, Participant.UserRole.PATIENT, patientDataModal.getPatientName());

    private String participantID;
    private String userName;
    private String jwtToken;
    private Boolean isOrganiser;

    private AuthenticatedUser.UserRole userRole;

//    public RoomCreationUserDTO(String participantID, String userName, String jwtToken, Participant.UserRole userRole) {
//        this.participantID = participantID;
//        this.userName = userName;
//        this.jwtToken = jwtToken;
//        this.userRole = userRole;
//    }

    public RoomCreationUserDTO(String participantID, String userName, String jwtToken, Boolean isOrganiser, AuthenticatedUser.UserRole userRole) {
        this.participantID = participantID;
        this.userName = userName;
        this.jwtToken = jwtToken;
        this.isOrganiser = isOrganiser;
        this.userRole = userRole;
    }


    //    public RoomCreationUserDTO(String participantID, String userName, Participant.UserRole userRole) {
//        this.participantID = participantID;
//        this.userName = userName;
//        this.userRole = userRole;
//    }
}
