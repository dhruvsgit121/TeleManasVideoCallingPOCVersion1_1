package com.example.ehrc.telemanas.Model.UpdatedModels;

import com.example.ehrc.telemanas.Model.Participant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
@Table(name = "updated_participant")

public class UpdatedParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialId;

    private LocalDateTime joinDate;

    private LocalDateTime leftDate;

    @Column(length = 1024)
    private String jwtToken;

    private boolean isOrganiser;

    private boolean hasJoinedRoom;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private UpdatedRoom room;


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UpdatedAuthenticatedUser authenticatedUser ;

    public void setRoom(UpdatedRoom room) {
        this.room = room;
    }

    public UpdatedParticipant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken, String participantId, boolean isOrganiser, boolean hasJoinedRoom, Participant.UserRole userRole, String userName) {
        this.joinDate = joinDate;
        this.leftDate = leftDate;
        this.jwtToken = jwtToken;
        this.isOrganiser = isOrganiser;
        this.hasJoinedRoom = hasJoinedRoom;
    }

    public UpdatedParticipant(Participant participant) {
        this.joinDate = participant.getJoinDate();
        this.leftDate = participant.getLeftDate();
        this.jwtToken = participant.getJwtToken();
        this.isOrganiser = participant.isOrganiser();
        this.hasJoinedRoom = participant.isHasJoinedRoom();
    }

    public boolean isOrganiser() {
        return isOrganiser;
    }

    public void setOrganiser(boolean organiser) {
        isOrganiser = organiser;
    }
}
