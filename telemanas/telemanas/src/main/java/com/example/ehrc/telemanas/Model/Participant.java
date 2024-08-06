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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialId;

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


    public Participant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken) {
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
}
