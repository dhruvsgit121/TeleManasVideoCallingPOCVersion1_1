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

@Entity(name = "video_consultation_consent_master")
public class VideoConsultationConsentMaster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String givenBy;

    private String status;

    private String description;

    private LocalDateTime creationDate;

    private LocalDateTime updationDate;

}
