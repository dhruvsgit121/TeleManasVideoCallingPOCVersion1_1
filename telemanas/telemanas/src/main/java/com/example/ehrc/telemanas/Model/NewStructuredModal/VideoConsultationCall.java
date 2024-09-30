package com.example.ehrc.telemanas.Model.NewStructuredModal;

import com.example.ehrc.telemanas.Service.NewStructuredService.VideoConsultationCallService;
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

@Entity(name = "video_consultation_call")
public class VideoConsultationCall implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String videoCallId;

    private LocalDateTime callStartTime;

    private LocalDateTime callEndTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VideoConsultationCallService.VideoCallStatus callStatus;

    private boolean isPatientConsent1Given;

    private boolean isPatientConsent2Given;

    private boolean isMHPConsentGiven;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VideoConsultationIDProof.VideoCallIDProofStatus isIDProofStatus;

    @OneToMany(mappedBy = "videoCall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoConsultationEvent> events = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "video_consultation_proof_id", nullable = true)  // Foreign key column
    private VideoConsultationIDProof videoCallIdProof;

    public VideoConsultationCall(String videoCallId, LocalDateTime callStartTime, LocalDateTime callEndTime, VideoConsultationCallService.VideoCallStatus callStatus, boolean isPatientConsent1Given, boolean isPatientConsent2Given, boolean isMHPConsentGiven, VideoConsultationIDProof.VideoCallIDProofStatus isIDProofStatus) {
        this.videoCallId = videoCallId;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;
        this.callStatus = callStatus;
        this.isPatientConsent1Given = isPatientConsent1Given;
        this.isPatientConsent2Given = isPatientConsent2Given;
        this.isMHPConsentGiven = isMHPConsentGiven;
        this.isIDProofStatus = isIDProofStatus;
    }

    public VideoConsultationCall(String videoCallId) {
        this(videoCallId, null, null, VideoConsultationCallService.VideoCallStatus.STARTED, false, false, false, VideoConsultationIDProof.VideoCallIDProofStatus.NOTVERIFIED);
    }
}
