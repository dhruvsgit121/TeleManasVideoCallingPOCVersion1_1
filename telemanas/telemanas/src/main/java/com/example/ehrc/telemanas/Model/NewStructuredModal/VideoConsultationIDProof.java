package com.example.ehrc.telemanas.Model.NewStructuredModal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity(name = "video_consultation_id_proof")
public class VideoConsultationIDProof implements Serializable {

    public enum VideoCallIDProofStatus {
        NOTVERIFIED,
        YES,
        NO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    private byte[] fileData;

    private String fileName;
    private String fileType;

    public VideoConsultationIDProof(byte[] fileData, String fileName, String fileType) {
        this.fileData = fileData;
        this.fileName = fileName;
        this.fileType = fileType;
    }
}
