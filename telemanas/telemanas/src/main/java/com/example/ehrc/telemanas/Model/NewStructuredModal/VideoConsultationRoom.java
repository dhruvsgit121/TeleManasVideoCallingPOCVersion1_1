package com.example.ehrc.telemanas.Model.NewStructuredModal;

//import com.example.ehrc.telemanas.Model.UpdatedModels.Event;
//import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString


@Entity(name = "video_consultation_room")
public class VideoConsultationRoom implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String roomId;

    private String roomShortCode;

    private LocalDateTime creationDate;

    private LocalDateTime expirationDate;

    //For Future User...
    private LocalDateTime updateDate;

    private boolean isActive;

//    id (pk)
//    room id uuid
//    room short code
//    create date
//    update date
//    expiration date
//    status


}



//@Getter
//@Setter
//@NoArgsConstructor
//@ToString
//
//@Entity
//public class Room implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long serialId;

//    @Column(nullable = false, unique = true, length = 20)
//    private String roomId;
//
//    @Column(nullable = false, unique = true, length = 20)
//    private String videoId;
//
//    private LocalDateTime creationDate;
//
//    private LocalDateTime expirationDate;
//
//    private boolean isActive;
//
//    private boolean isDoctorConsentProvided;
//
//    private boolean isPatientConsentProvided;
//
//    private String roomShortCode;

//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Participant> participants = new ArrayList<>();
//
//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Event> events = new ArrayList<>();
//
//    public boolean isActive() {
//        return isActive;
//    }
//
//    public void setActive(boolean active) {
//        isActive = active;
//    }
//
//    public Room(String roomId, String videoId, LocalDateTime creationDate, LocalDateTime expirationDate, boolean isActive, String roomShortCode, boolean isDoctorConsentProvided) {
//        this.roomId = roomId;
//        this.videoId = videoId;
//        this.creationDate = creationDate;
//        this.expirationDate = expirationDate;
//        this.isActive = isActive;
//        this.roomShortCode = roomShortCode;
//        this.isDoctorConsentProvided = isDoctorConsentProvided;
//    }
//
//    // Utility method to add a participant...
//    public void addParticipant(Participant participant) {
//        participants.add(participant);
//        participant.setRoom(this);
//    }
//
//    // Utility method to add a Event...
//    public void addEvent(Event event) {
//        events.add(event);
//        event.setRoom(this);
//    }
//}

