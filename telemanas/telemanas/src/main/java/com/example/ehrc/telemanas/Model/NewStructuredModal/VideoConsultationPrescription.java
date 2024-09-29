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


@Entity(name = "video_consultation_prescription")
public class VideoConsultationPrescription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private LocalDateTime generatedData;

    private String callIvrsID;

    @OneToOne
    @JoinColumn(name = "video_consultation_call_id", nullable = false)  // Foreign key column
    private VideoConsultationCall videoCallData;

    public VideoConsultationPrescription(LocalDateTime generatedData, String callIvrsID) {
        this.generatedData = generatedData;
        this.callIvrsID = callIvrsID;
    }
}
