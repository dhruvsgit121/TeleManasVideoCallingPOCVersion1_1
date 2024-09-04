package com.example.ehrc.telemanas.Model.UpdatedModels;

import com.example.ehrc.telemanas.Model.Participant;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Updated_autheticated_user")
public class UpdatedAuthenticatedUser implements Serializable {

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
    private Participant.UserRole userRole;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String participantId;

    @Column(nullable = false)
    private String decryptedMobileNumber;

    public UpdatedAuthenticatedUser(Participant.UserRole userRole, String userName, String participantId, String decryptedMobileNumber) {
        this.userRole = userRole;
        this.userName = userName;
        this.participantId = participantId;
        this.decryptedMobileNumber = decryptedMobileNumber;
    }

    public UpdatedAuthenticatedUser() {
    }

    @OneToMany(mappedBy = "authenticatedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UpdatedParticipant> updatedParticipants = new HashSet<>();

    // Utility method to add a participant...
    public void setAuthenticatedUser(UpdatedParticipant authenticatedParticipant) {
        updatedParticipants.add(authenticatedParticipant);
        authenticatedParticipant.setAuthenticatedUser(this);
    }
}
