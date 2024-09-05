//package com.example.ehrc.telemanas.Model;
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.Set;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@ToString
//
//@Entity
//public class Room {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long serialId;
//
//    @Column(nullable = false, unique = true, length = 20)
//    private String roomId;
//
//    @Column(nullable = false, unique = true, length = 20)
//    private String videoId;
//
//    private LocalDateTime creationDate;
//
//    private LocalDateTime expirationDate;
//
//    private boolean isActive;
//
//    private String roomShortCode;
//
//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private Set<Participant> participants = new HashSet<>();
//
//    public Room(String roomId, String videoId, LocalDateTime creationDate, LocalDateTime expirationDate, boolean isActive, String roomShortCode) {
//        this.roomId = roomId;
//        this.videoId = videoId;
//        this.creationDate = creationDate;
//        this.expirationDate = expirationDate;
//        this.isActive = isActive;
//        this.roomShortCode = roomShortCode;
//    }
//
//    //    public Room(String roomId, LocalDateTime creationDate, LocalDateTime expirationDate, boolean isActive, String roomShortCode) {
////        this.roomId = roomId;
////        this.creationDate = creationDate;
////        this.expirationDate = expirationDate;
////        this.isActive = isActive;
////        this.roomShortCode = roomShortCode;
////    }
//
//
//    // Utility method to add a participant...
//    public void addParticipant(Participant participant) {
//        participants.add(participant);
//        participant.setRoom(this);
//    }
//}
