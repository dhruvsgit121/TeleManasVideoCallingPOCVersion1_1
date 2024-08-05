package com.example.ehrc.telemanas.Controller;

import com.example.ehrc.telemanas.Model.User;
import com.example.ehrc.telemanas.Service.UserService;
import org.hibernate.annotations.processing.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/helloworld")
    public String getHelloWorld() {
        return "Hello World!!!";
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User createdUser = userService.saveUser(user);
        return new ResponseEntity(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/createusers")
    public ResponseEntity<User> saveAllUsers(@RequestBody List<User> users) {

        List<User> createdUsers = null;
        try {
            createdUsers = userService.saveAllUsers(users);
        }
        catch (Exception ex){
            System.out.println("Get into sql exception block");
            System.out.println(ex);
            System.out.println("message is : " + ex.getMessage());
        }


        return new ResponseEntity(createdUsers, HttpStatus.CREATED);
    }
}

