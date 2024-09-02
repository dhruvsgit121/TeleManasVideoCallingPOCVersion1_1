package com.example.ehrc.telemanas.Model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor

@Entity
public class Participant {

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
    private UserRole userRole;

    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
    private String userName;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonBackReference
    // This is the foreign key column
    private Room room;

    @Override
    public String toString() {
        return "Participant{" +
                "serialId=" + serialId +
                ", joinDate=" + joinDate +
                ", leftDate=" + leftDate +
                ", jwtToken='" + jwtToken + '\'' +
                ", participantId='" + participantId + '\'' +
                ", isOrganiser=" + isOrganiser +
                ", hasJoinedRoom=" + hasJoinedRoom +
                ", userRole=" + userRole +
                ", userName=" + userName +
                ", room=" + room +
                '}';
    }

    //    @Override
//    public String toString() {
//        return "Participant{" +
//                "serialId=" + serialId +
//                ", joinDate=" + joinDate +
//                ", leftDate=" + leftDate +
//                ", jwtToken='" + jwtToken + '\'' +
//                ", participantId='" + participantId + '\'' +
//                ", isOrganiser=" + isOrganiser +
//                ", userRole=" + userRole +
//                ", room=" + room +
//                '}';
//    }


    public Participant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken, String participantId, boolean isOrganiser, boolean hasJoinedRoom, UserRole userRole, String userName, Room room) {
        this.joinDate = joinDate;
        this.leftDate = leftDate;
        this.jwtToken = jwtToken;
        this.participantId = participantId;
        this.isOrganiser = isOrganiser;
        this.hasJoinedRoom = hasJoinedRoom;
        this.userRole = userRole;
        this.userName = userName;
        this.room = room;
    }

    public Participant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken, String participantId, UserRole userRole) {
        this.joinDate = joinDate;
        this.leftDate = leftDate;
        this.jwtToken = jwtToken;
        this.participantId = participantId;
        this.userRole = userRole;
    }

    public Participant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken, String participantId, boolean isOrganiser, UserRole userRole, String userName) {
        this.joinDate = joinDate;
        this.leftDate = leftDate;
        this.jwtToken = jwtToken;
        this.participantId = participantId;
        this.isOrganiser = isOrganiser;
        this.userRole = userRole;
        this.userName = userName;
    }

    public Participant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken) {
        this.joinDate = joinDate;
        this.leftDate = leftDate;
        this.jwtToken = jwtToken;
    }

    public Participant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken, String participantId) {
        this.joinDate = joinDate;
        this.leftDate = leftDate;
        this.jwtToken = jwtToken;
        this.participantId = participantId;
    }

    //    public Participant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken, Long participantId) {
//        this.joinDate = joinDate;
//        this.leftDate = leftDate;
//        this.jwtToken = jwtToken;
//        this.participantId = participantId;
//    }
}
