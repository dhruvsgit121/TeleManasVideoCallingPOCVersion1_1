package com.example.ehrc.telemanas.Model.NewStructuredModal;

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

@Entity(name = "video_consultation_event_master")
public class VideoConsultationEventMaster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private boolean status;

    private LocalDateTime creationDate;

    private LocalDateTime updationDate;

    @OneToMany(mappedBy = "videoCallEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoConsultationEvent> events = new ArrayList<>();

    public VideoConsultationEventMaster(String name, boolean status, LocalDateTime creationDate, LocalDateTime updationDate) {
        this.name = name;
        this.status = status;
        this.creationDate = creationDate;
        this.updationDate = null;
    }
}
