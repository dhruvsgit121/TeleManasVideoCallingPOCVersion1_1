package com.example.ehrc.telemanas.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor


@Entity
public class UserIdentity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long identityId;

    private String name;

    private String type;

    @Lob
    private byte[] data;

    private String roomShortCode;

    private boolean isIdentityVerified = false;

    public UserIdentity(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public UserIdentity(String name, String type, byte[] data, String roomShortCode) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.roomShortCode = roomShortCode;
    }

}
