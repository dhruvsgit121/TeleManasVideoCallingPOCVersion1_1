package com.example.ehrc.telemanas.Model.UpdatedModels;


import com.example.ehrc.telemanas.Model.Participant;
//import com.example.ehrc.telemanas.Model.Room;
//import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "updated_participant")

public class UpdatedParticipant {

    public enum UserRole {
        MHP,
        PATIENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialId;

    private LocalDateTime joinDate;

    private LocalDateTime leftDate;

    @Column(length = 1024)
    private String jwtToken;

    private String participantId;

    private boolean isOrganiser;

    private boolean hasJoinedRoom;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Participant.UserRole userRole;

    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
    private String userName;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private UpdatedRoom room;

//    public UpdatedRoom getRoom() {
//        return room;
//    }
//
    public void setRoom(UpdatedRoom room) {
        this.room = room;
    }

//    public Set<UpdatedRoom> getUpdatedroom() {
//        return updatedroom;
//    }
//
//    public void setUpdatedroom(Set<UpdatedRoom> updatedroom) {
//        this.updatedroom = updatedroom;
//    }

//    @ManyToMany
//    @JoinTable(
//            name = "participant_room",
//            joinColumns = @JoinColumn(name = "participant_id"),
//            inverseJoinColumns = @JoinColumn(name = "room_id")
//    )
//    private Set<UpdatedRoom> updatedroom;


//    @ManyToOne
//    @JoinColumn(name = "room_id")
//    @JsonBackReference
//    // This is the foreign key column
//    private Room room;


    public UpdatedParticipant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken, String participantId, boolean isOrganiser, boolean hasJoinedRoom, Participant.UserRole userRole, String userName) {
        this.joinDate = joinDate;
        this.leftDate = leftDate;
        this.jwtToken = jwtToken;
        this.participantId = participantId;
        this.isOrganiser = isOrganiser;
        this.hasJoinedRoom = hasJoinedRoom;
        this.userRole = userRole;
        this.userName = userName;
    }

//    public LocalDateTime getJoinDate() {
//        return joinDate;
//    }
//
//    public void setJoinDate(LocalDateTime joinDate) {
//        this.joinDate = joinDate;
//    }
//
//    public LocalDateTime getLeftDate() {
//        return leftDate;
//    }
//
//    public void setLeftDate(LocalDateTime leftDate) {
//        this.leftDate = leftDate;
//    }
//
//    public String getJwtToken() {
//        return jwtToken;
//    }
//
//    public void setJwtToken(String jwtToken) {
//        this.jwtToken = jwtToken;
//    }
//
//    public String getParticipantId() {
//        return participantId;
//    }
//
//    public void setParticipantId(String participantId) {
//        this.participantId = participantId;
//    }

    public boolean isOrganiser() {
        return isOrganiser;
    }

    public void setOrganiser(boolean organiser) {
        isOrganiser = organiser;
    }

//    public boolean isHasJoinedRoom() {
//        return hasJoinedRoom;
//    }
//
//    public void setHasJoinedRoom(boolean hasJoinedRoom) {
//        this.hasJoinedRoom = hasJoinedRoom;
//    }
//
//    public Participant.UserRole getUserRole() {
//        return userRole;
//    }
//
//    public void setUserRole(Participant.UserRole userRole) {
//        this.userRole = userRole;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
}
