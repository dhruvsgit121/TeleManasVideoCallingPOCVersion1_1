package com.example.ehrc.telemanas.Model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
//import java.sql.Date;
import java.time.LocalDateTime;
//import java.util.Set;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialId;

//    @Column(nullable = false, unique = true, length = 20)
//    private String roomName;

    private LocalDateTime joinDate;

    private LocalDateTime leftDate;

    @Column(length = 1024)
    private String jwtToken;

    private Long participantId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonBackReference
    // This is the foreign key column
    private Room room;


    public Participant() {
    }


    //String roomName,

    public Participant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken) {
//        this.roomName = roomName;
        this.joinDate = joinDate;
        this.leftDate = leftDate;
        this.jwtToken = jwtToken;
    }


    public Participant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken, Long participantId) {
        this.joinDate = joinDate;
        this.leftDate = leftDate;
        this.jwtToken = jwtToken;
        this.participantId = participantId;
    }

//    @Override
//    public String toString() {
//        return "Participant{" +
//                "serialId=" + serialId +
//                ", joinDate=" + joinDate +
//                ", leftDate=" + leftDate +
//                ", jwtToken='" + jwtToken + '\'' +
//                ", participantId=" + participantId +
//                ", room=" + room +
//                '}';
//    }

    public Long getSerialId() {
        return serialId;
    }

    public void setSerialId(Long serialId) {
        this.serialId = serialId;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDateTime getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(LocalDateTime leftDate) {
        this.leftDate = leftDate;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }


//

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }
}
