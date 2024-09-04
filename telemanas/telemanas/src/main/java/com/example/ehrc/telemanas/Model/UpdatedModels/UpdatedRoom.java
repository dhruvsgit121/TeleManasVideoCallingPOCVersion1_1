package com.example.ehrc.telemanas.Model.UpdatedModels;

//import com.example.ehrc.telemanas.Model.Participant;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.example.ehrc.telemanas.Model.Participant;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
//import java.util.HashSet;
//import java.util.Set;

//import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;


@Entity
@Table(name = "updated_room")
public class UpdatedRoom implements Serializable {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "name")
//    private String name;
//
//
//

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

    @OneToMany(mappedBy = "updatedroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UpdatedParticipant> participants = new HashSet<>();

    public Set<UpdatedParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<UpdatedParticipant> participants) {
        this.participants = participants;
    }

    public UpdatedRoom(String roomId, String videoId, LocalDateTime creationDate, LocalDateTime expirationDate, boolean isActive, String roomShortCode) {
        this.roomId = roomId;
        this.videoId = videoId;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
        this.roomShortCode = roomShortCode;
    }



    // Utility method to add a participant...
    public void addParticipant(UpdatedParticipant participant) {
        participants.add(participant);
        participant.setRoom(this);
    }
    //    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private Set<Participant> participants = new HashSet<>();


}
