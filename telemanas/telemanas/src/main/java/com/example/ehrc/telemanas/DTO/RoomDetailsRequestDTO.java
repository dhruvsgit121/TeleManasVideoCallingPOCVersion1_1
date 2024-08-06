package com.example.ehrc.telemanas.DTO;

public class RoomDetailsRequestDTO {

    private String roomShortCode;

    private int isMHP;

    public String getRoomShortCode() {
        return roomShortCode;
    }

    public void setRoomShortCode(String roomShortCode) {
        this.roomShortCode = roomShortCode;
    }

    public int getIsMHP() {
        return isMHP;
    }

    public void setIsMHP(int isMHP) {
        this.isMHP = isMHP;
    }

    //
//    public boolean isMHP() {
//        return isMHP;
//    }
//
//    public void setMHP(boolean MHP) {
//        isMHP = MHP;
//    }

    @Override
    public String toString() {
        return "RoomDetailsRequestDTO{" +
                "roomShortCode='" + roomShortCode + '\'' +
                ", isMHP=" + isMHP +
                '}';
    }
}
