package com.example.ehrc.telemanas.Model.NewStructuredModal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor


@Entity(name = "video_consultation_participant")
public class VideoConsultationParticipant implements Serializable {

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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private VideoConsultationUser user;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoConsultationEvent> events = new ArrayList<>();

    public VideoConsultationParticipant(String jwt_token, String participantID, boolean isOrganiser) {
        this.jwt_token = jwt_token;
        this.participantID = participantID;
        this.isOrganiser = isOrganiser;
    }
}
