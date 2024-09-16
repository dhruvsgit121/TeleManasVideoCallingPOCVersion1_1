package com.example.ehrc.telemanas.Model.UpdatedModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor


@Entity
public class Event implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long eventId;

    private LocalDateTime eventTimeStamp;

    private String eventDescription;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    public Event(LocalDateTime eventTimeStamp, String eventDescription) {
        this.eventTimeStamp = eventTimeStamp;
        this.eventDescription = eventDescription;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
