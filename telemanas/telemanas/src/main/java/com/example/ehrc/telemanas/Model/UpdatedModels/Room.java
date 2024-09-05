package com.example.ehrc.telemanas.Model.UpdatedModels;

import jakarta.persistence.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

@Entity
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long serialId;

    @Column(nullable = false, unique = true, length = 20)
    private String roomId;

    @Column(nullable = false, unique = true, length = 20)
    private String videoId;

    private LocalDateTime creationDate;

    private LocalDateTime expirationDate;

    private boolean isActive;

    private String roomShortCode;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Room(String roomId, String videoId, LocalDateTime creationDate, LocalDateTime expirationDate, boolean isActive, String roomShortCode) {
        this.roomId = roomId;
        this.videoId = videoId;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
        this.roomShortCode = roomShortCode;
    }

    // Utility method to add a participant...
    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setRoom(this);
    }
}
