package com.example.ehrc.telemanas.Model.NewStructuredModal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity(name = "video_consultation_event")
public class VideoConsultationEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "video_consultation_call_id", referencedColumnName = "id")
    private VideoConsultationCall videoCall;

    @ManyToOne
    @JoinColumn(name = "video_consultation_event_id", referencedColumnName = "id")
    private VideoConsultationEventMaster videoCallEvent;

    @ManyToOne
    @JoinColumn(name = "video_consultation_participant_id", referencedColumnName = "id")
    private VideoConsultationParticipant participant;

    private LocalDateTime eventTimeStamp;

    public VideoConsultationEvent(LocalDateTime eventTimeStamp) {
        this.eventTimeStamp = eventTimeStamp;
    }
}
