package com.example.ehrc.telemanas.Model.NewStructuredModal;


import com.example.ehrc.telemanas.Model.UpdatedModels.AuthenticatedUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//@NoArgsConstructor
@ToString

@Entity(name = "video_consultation_user")
public class VideoConsultationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String userName;

    private String userID;

    private String userDesignation;

    private String userRegistrationNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoConsultationParticipant> participants = new ArrayList<>();

    public void addParticipant(VideoConsultationParticipant participant) {
        participants.add(participant);
        participant.setUser(this);
    }

    public VideoConsultationUser() {
    }

    public VideoConsultationUser(String userName, String userID, String userDesignation, String userRegistrationNumber) {
        this.userName = userName;
        this.userID = userID;
        this.userDesignation = userDesignation;
        this.userRegistrationNumber = userRegistrationNumber;
    }

    public VideoConsultationUser(String userName, String userID) {
        this.userName = userName;
        this.userID = userID;
        this.userDesignation = "";
        this.userRegistrationNumber = "";
    }


    //    private LocalDateTime creationDate;
//
//    private LocalDateTime expirationDate;


//    responseData.put("jwtToken", firstParticipant.getJwtToken());
//        responseData.put("userName", firstParticipant.getAuthenticatedUser().getUserName());
//        responseData.put("participatingUserName", secondParticipant.getAuthenticatedUser().getUserName());
//        responseData.put("userId", firstParticipant.getAuthenticatedUser().getParticipantId());
//        responseData.put("participatingUserId", secondParticipant.getAuthenticatedUser().getParticipantId());
//        responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), firstParticipant.getJwtToken()));
//
//        if (roomDetailsRequest.getIsMHP() != 1) {
//
//        AuthenticatedUser MHPUser = firstParticipant.getAuthenticatedUser().getUserRole().equals(AuthenticatedUser.UserRole.MHP) ? firstParticipant.getAuthenticatedUser() : secondParticipant.getAuthenticatedUser();
//
//        String clientID = MHPUser.getParticipantId();
//        String roleDisplayName = MHPUser.getRoleDisplayName();
//        //firstParticipant.getAuthenticatedUser().getUserRole().equals(AuthenticatedUser.UserRole.MHP) ? firstParticipant.getAuthenticatedUser().getParticipantId() : secondParticipant.getAuthenticatedUser().getParticipantId();
//        responseData.put("clientID", clientID);
//        responseData.put("MHPRegistrationNumber", "MHP1234NHNOPA");
//        responseData.put("Designation", roleDisplayName);
//    }
//}

}
