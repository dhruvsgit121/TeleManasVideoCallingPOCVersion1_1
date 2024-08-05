package com.example.ehrc.telemanas.Model;


import jakarta.persistence.*;
import java.sql.Date;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialId;

    @Column(nullable = false, unique = false)
    private Long participantId;

    @Column(nullable = false, unique = true, length = 20)
    private String roomId;

    private Date joinDate;

    private Date leftDate;

    private String jwtToken;

}
