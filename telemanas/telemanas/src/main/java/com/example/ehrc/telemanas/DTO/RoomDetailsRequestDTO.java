package com.example.ehrc.telemanas.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomDetailsRequestDTO {

    private String roomShortCode;
    private int isMHP;
    private int isPatientConsentProvided;

    public RoomDetailsRequestDTO() {
    }

    public RoomDetailsRequestDTO(String roomShortCode, int isMHP) {
        this.roomShortCode = roomShortCode;
        this.isMHP = isMHP;
    }

    public RoomDetailsRequestDTO(CallStartDTO callStartDTO) {
        this.roomShortCode = callStartDTO.getRoomShortCode();
        this.isMHP = callStartDTO.getIsMHP();
    }

}
