package com.example.ehrc.telemanas.Model.UpdatedModels;

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

    private boolean isOrganiser;

    private boolean hasJoinedRoom;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private AuthenticatedUser authenticatedUser ;

    public void setRoom(Room room) {
        this.room = room;
    }

    public Participant(LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken, boolean isOrganiser, boolean hasJoinedRoom) {
        this.joinDate = joinDate;
        this.leftDate = leftDate;
        this.jwtToken = jwtToken;
        this.isOrganiser = isOrganiser;
        this.hasJoinedRoom = hasJoinedRoom;
    }
}
