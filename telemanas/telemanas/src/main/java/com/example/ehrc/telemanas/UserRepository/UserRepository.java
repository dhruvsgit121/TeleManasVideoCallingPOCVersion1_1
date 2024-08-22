//package com.example.ehrc.telemanas.UserRepository;
//
//import com.example.ehrc.telemanas.Model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;

//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//
//    @Query(value = "SELECT * FROM user WHERE user_id = :user_id AND user_role = :user_role", nativeQuery = true)
//    Optional<User> findUserByIDAndRole(@Param("user_id") Long user_id, @Param("user_role") String user_role);
//
//}
