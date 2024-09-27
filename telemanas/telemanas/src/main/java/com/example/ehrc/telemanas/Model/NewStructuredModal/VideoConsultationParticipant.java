package com.example.ehrc.telemanas.Model.NewStructuredModal;

import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString


@Entity(name = "video_consultation_participant")
public class VideoConsultationParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 1024)
    private String jwt_token;

    private String participantID;

    private boolean isOrganiser;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private VideoConsultationRoom room;

    public VideoConsultationParticipant(String jwt_token, String participantID, boolean isOrganiser) {
        this.jwt_token = jwt_token;
        this.participantID = participantID;
        this.isOrganiser = isOrganiser;
    }


    //    id (pk)
//
//    is organizer
//    participant id (telemanas/user)
//    jwt_token

//    room id (fk)

//    public void setRoom(VideoConsultationRoom room) {
//        this.room = room;
//    }

//    // Utility method to add a participant...
//    public void addParticipant(VideoConsultationParticipant participant) {
//        participants.add(participant);
//        participant.setRoom(this);
//    }

    //roomid


//    private LocalDateTime creationDate;
//
//    private LocalDateTime expirationDate;

    //For Future User...
//    private LocalDateTime updateDate;




//    id (pk)
//    room id (fk)
//    is organizer
//    participant id (telemanas/user)
//    jwt_token



}
