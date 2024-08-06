package com.example.ehrc.telemanas.Model;


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

    private String jwtToken;

    private Long participantId;

    @ManyToOne
    @JoinColumn(name = "room_id") // This is the foreign key column
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
