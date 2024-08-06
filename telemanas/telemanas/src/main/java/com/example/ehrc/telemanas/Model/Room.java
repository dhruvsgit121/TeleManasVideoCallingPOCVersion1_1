package com.example.ehrc.telemanas.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialId;

    @Column(nullable = false, unique = true, length = 20)

    private String roomId;

    private LocalDateTime creationDate;

    private LocalDateTime expirationDate;

    private boolean isActive;

    private String roomShortCode;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Participant> participants = new HashSet<>();


    public Room() {
    }

    public Room(String roomId, LocalDateTime creationDate, LocalDateTime expirationDate, boolean isActive, String roomShortCode) {
        this.roomId = roomId;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
        this.roomShortCode = roomShortCode;
    }


    // Utility method to add a book to the author
    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setRoom(this);
    }

    // Utility method to remove a book from the author
    public void removeParticipant(Participant participant) {
        participants.remove(participant);
        participant.setRoom(null);
    }

    public Long getSerialId() {
        return serialId;
    }

    public void setSerialId(Long serialId) {
        this.serialId = serialId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRoomShortCode() {
        return roomShortCode;
    }

    public void setRoomShortCode(String roomShortCode) {
        this.roomShortCode = roomShortCode;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    //    public Set<Participant> getPraticipants() {
//        return praticipants;
//    }
//
//    public void setPraticipants(Set<Participant> praticipants) {
//        this.praticipants = praticipants;
//    }

    //    public Room(String roomId, Date creationDate, Date expirationDate, boolean isActive, String roomShortCode) {
//        this.serialId = serialId;
//        this.roomId = roomId;
//        this.creationDate = creationDate;
//        this.expirationDate = expirationDate;
//        this.isActive = isActive;
//        this.roomShortCode = roomShortCode;
//    }
//
//    public Long getSerialId() {
//        return serialId;
//    }
//
//    public void setSerialId(Long serialId) {
//        this.serialId = serialId;
//    }
//
//    public String getRoomId() {
//        return roomId;
//    }
//
//    public void setRoomId(String roomId) {
//        this.roomId = roomId;
//    }
//
//    public Date getCreationDate() {
//        return creationDate;
//    }
//
//    public void setCreationDate(Date creationDate) {
//        this.creationDate = creationDate;
//    }
//
//    public Date getExpirationDate() {
//        return expirationDate;
//    }
//
//    public void setExpirationDate(Date expirationDate) {
//        this.expirationDate = expirationDate;
//    }
//
//    public boolean isActive() {
//        return isActive;
//    }
//
//    public void setActive(boolean active) {
//        isActive = active;
//    }
//
//    public String getRoomShortCode() {
//        return roomShortCode;
//    }
//
//    public void setRoomShortCode(String roomShortCode) {
//        this.roomShortCode = roomShortCode;
//    }
}
