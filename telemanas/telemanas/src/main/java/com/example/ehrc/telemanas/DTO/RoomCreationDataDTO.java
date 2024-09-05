package com.example.ehrc.telemanas.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class RoomCreationDataDTO {

    private String roomID;
    private String roomShortCode;
    private String videoID;

    public RoomCreationDataDTO(String roomID, String roomShortCode, String videoID) {
        this.roomID = roomID;
        this.roomShortCode = roomShortCode;
        this.videoID = videoID;
    }
}
