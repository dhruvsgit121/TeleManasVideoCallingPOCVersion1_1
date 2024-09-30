package com.example.ehrc.telemanas.Model.NewStructuredModal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString

@Entity(name = "video_consultation_user")
public class VideoConsultationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String userName;

    private String userID;

    private String userDesignation;

    private String userRegistrationNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoConsultationParticipant> participants = new ArrayList<>();

    public void addParticipant(VideoConsultationParticipant participant) {
        participants.add(participant);
        participant.setUser(this);
    }

    public VideoConsultationUser() {
    }

    public VideoConsultationUser(String userName, String userID, String userDesignation, String userRegistrationNumber) {
        this.userName = userName;
        this.userID = userID;
        this.userDesignation = userDesignation;
        this.userRegistrationNumber = userRegistrationNumber;
    }

    public VideoConsultationUser(String userName, String userID) {
        this.userName = userName;
        this.userID = userID;
        this.userDesignation = "";
        this.userRegistrationNumber = "";
    }
}
