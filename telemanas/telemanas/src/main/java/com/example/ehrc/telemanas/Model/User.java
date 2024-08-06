package com.example.ehrc.telemanas.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {

    public enum UserRole {
        MHP,
        PATIENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false, unique = true)
    private String contactNumber;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;


//    @Override
//    public String toString() {
//        return "User{" +
//                "userId=" + userId +
//                ", userName='" + userName + '\'' +
//                ", contactNumber='" + contactNumber + '\'' +
//                ", userEmail='" + userEmail + '\'' +
//                ", userRole=" + userRole +
//                '}';
//    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
