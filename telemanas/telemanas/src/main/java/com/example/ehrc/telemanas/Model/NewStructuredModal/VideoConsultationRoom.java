package com.example.ehrc.telemanas.Model.NewStructuredModal;

import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

//    private String active;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoConsultationParticipant> participants = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)  // Foreign key column
    private VideoConsultationStatusMaster status; // Reference to VideoConsultationStatusMaster

    @OneToOne
    @JoinColumn(name = "video_consultation_call_id", nullable = false)  // Foreign key column
    private VideoConsultationCall videoCallData;

//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<VideoConsultationParticipant> participants = new ArrayList<>();

    // Utility method to add a participant...
    public void addParticipant(VideoConsultationParticipant participant) {
        participants.add(participant);
        participant.setRoom(this);
    }

//    public VideoConsultationRoom(String roomId, String roomShortCode, LocalDateTime creationDate, LocalDateTime expirationDate, LocalDateTime updateDate, boolean isActive) {
//        this.roomId = roomId;
//        this.roomShortCode = roomShortCode;
//        this.creationDate = creationDate;
//        this.expirationDate = expirationDate;
//        this.updateDate = updateDate;
//        this.isActive = isActive;
//    }


    public VideoConsultationRoom(String roomId, String roomShortCode, LocalDateTime creationDate, LocalDateTime expirationDate, LocalDateTime updateDate, boolean isActive) {
        this.roomId = roomId;
        this.roomShortCode = roomShortCode;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.updateDate = updateDate;
        this.isActive = isActive;
//        this.active = active;
    }
}
