package com.example.ehrc.telemanas.Model;


import jakarta.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialId;

    @Column(nullable = false, unique = true, length = 20)
    private String roomId;

    private LocalDateTime joinDate;

    private LocalDateTime leftDate;

    private String jwtToken;

    @ManyToOne
    @JoinColumn(name = "participant_id") // This is the foreign key column
    private User user;

    public Participant() {
    }

    public Participant(String roomId, LocalDateTime joinDate, LocalDateTime leftDate, String jwtToken) {
        this.roomId = roomId;
        this.joinDate = joinDate;
        this.leftDate = leftDate;
        this.jwtToken = jwtToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


    //    @OneToMany(mappedBy = "participant")
//    private Set<User> users;


//    @Man
//    private User user;

//}
