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

}
