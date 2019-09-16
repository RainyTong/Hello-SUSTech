package com.example.hellosustechbackend.api;

import java.util.List;

import com.example.hellosustechbackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Store the methods to do user-related operations on mysql database
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Get {@code User} objects from the database by mail address
     *
     * @param mailAddress the mail address
     * @return {@code List} object that storing all found {@code User} objects
     */
    List<User> findUserByMailAddress(String mailAddress);
    /**
     * Get {@code User} objects from the database by user name
     *
     * @param username the user's name
     * @return {@code List} object that storing all found {@code User} objects
     */
    List<User> findUserByUsername(String username);

    User findUserById(int id);
}
