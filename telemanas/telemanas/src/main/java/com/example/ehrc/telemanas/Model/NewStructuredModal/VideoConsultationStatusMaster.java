package com.example.ehrc.telemanas.Model.NewStructuredModal;


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


@Entity(name = "video_consultation_status_master")
public class VideoConsultationStatusMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private boolean status;

    private LocalDateTime creationDate;

    private LocalDateTime updationDate;

//    @OneToOne
//    @JoinColumn(name = "room_id", nullable = false, unique = true)
//    private VideoConsultationRoom room;

}
