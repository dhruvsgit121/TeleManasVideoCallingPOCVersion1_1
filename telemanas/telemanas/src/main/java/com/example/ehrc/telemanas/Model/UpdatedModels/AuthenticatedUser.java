package com.example.ehrc.telemanas.Model.UpdatedModels;

//import com.example.ehrc.telemanas.Model.Participant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor

@Entity
//@Table(name = "Updated_authenticated_user")
public class AuthenticatedUser implements Serializable {

    public enum UserRole {
        MHP,
        PATIENT
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long serialId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthenticatedUser.UserRole userRole;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String participantId;

    @Column(nullable = false)
    private String decryptedMobileNumber;

    public AuthenticatedUser(AuthenticatedUser.UserRole userRole, String userName, String participantId, String decryptedMobileNumber) {
        this.userRole = userRole;
        this.userName = userName;
        this.participantId = participantId;
        this.decryptedMobileNumber = decryptedMobileNumber;
    }

//    public UpdatedAuthenticatedUser() {
//    }

    @OneToMany(mappedBy = "authenticatedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    // Utility method to add a participant...
    public void setAuthenticatedUser(Participant authenticatedParticipant) {
        participants.add(authenticatedParticipant);
        authenticatedParticipant.setAuthenticatedUser(this);
    }
}
