package com.example.ehrc.telemanas.Service;

import com.example.ehrc.telemanas.Model.User;
import com.example.ehrc.telemanas.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<User> saveAllUsers(List<User> user) throws SQLException {
        return userRepository.saveAll(user);
    }


    public User getUserByID(long userID){
        return userRepository.getReferenceById(userID);
    }

}
