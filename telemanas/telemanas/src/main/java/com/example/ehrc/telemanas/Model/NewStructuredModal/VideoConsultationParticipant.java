package com.example.ehrc.telemanas.Model.NewStructuredModal;

//import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
//import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
//import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString


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

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoConsultationEvent> events = new ArrayList<>();

    public VideoConsultationParticipant(String jwt_token, String participantID, boolean isOrganiser) {
        this.jwt_token = jwt_token;
        this.participantID = participantID;
        this.isOrganiser = isOrganiser;
    }
}
