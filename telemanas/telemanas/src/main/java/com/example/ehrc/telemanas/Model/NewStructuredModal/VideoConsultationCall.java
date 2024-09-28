package com.example.ehrc.telemanas.Model.NewStructuredModal;

import com.example.ehrc.telemanas.Model.UpdatedModels.AuthenticatedUser;
import com.example.ehrc.telemanas.Service.NewStructuredService.VideoConsultationCallService;
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

//    private String callStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VideoConsultationCallService.VideoCallStatus callStatus;

    private boolean isPatientConsent1Given;

    private boolean isPatientConsent2Given;

    private boolean isMHPConsentGiven;

    private boolean isIDProofChecked;

//    @OneToOne
//    @JoinColumn(name = "room_id", nullable = false)  // Foreign key column
//    private VideoConsultationRoom room;

//    public VideoConsultationCall(String videoCallId, LocalDateTime callStartTime, LocalDateTime callEndTime, String callStatus, boolean isPatientConsent1Given, boolean isPatientConsent2Given, boolean isMHPConsentGiven, boolean isIDProofChecked) {
//        this.videoCallId = videoCallId;
//        this.callStartTime = callStartTime;
//        this.callEndTime = callEndTime;
//        this.callStatus = callStatus;
//        this.isPatientConsent1Given = isPatientConsent1Given;
//        this.isPatientConsent2Given = isPatientConsent2Given;
//        this.isMHPConsentGiven = isMHPConsentGiven;
//        this.isIDProofChecked = isIDProofChecked;
//    }

    public VideoConsultationCall(String videoCallId, LocalDateTime callStartTime, LocalDateTime callEndTime, VideoConsultationCallService.VideoCallStatus callStatus, boolean isPatientConsent1Given, boolean isPatientConsent2Given, boolean isMHPConsentGiven, boolean isIDProofChecked) {
        this.videoCallId = videoCallId;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;
        this.callStatus = callStatus;
        this.isPatientConsent1Given = isPatientConsent1Given;
        this.isPatientConsent2Given = isPatientConsent2Given;
        this.isMHPConsentGiven = isMHPConsentGiven;
        this.isIDProofChecked = isIDProofChecked;
    }

    public VideoConsultationCall(String videoCallId) {

        this(videoCallId, null, null, VideoConsultationCallService.VideoCallStatus.STARTED, false, false, false, false);
//        this.videoCallId = videoCallId;
    }

    //    public VideoConsultationCall(String videoCallId, String callStatus, boolean isPatientConsent1Given, boolean isPatientConsent2Given, boolean isMHPConsentGiven, boolean isIDProofChecked) {
//        this.videoCallId = videoCallId;
//        this.callStatus = callStatus;
//        this.isPatientConsent1Given = isPatientConsent1Given;
//        this.isPatientConsent2Given = isPatientConsent2Given;
//        this.isMHPConsentGiven = isMHPConsentGiven;
//        this.isIDProofChecked = isIDProofChecked;
//    }

    //Foreign Keys...
    //room id (fk)
    //proof id
}
