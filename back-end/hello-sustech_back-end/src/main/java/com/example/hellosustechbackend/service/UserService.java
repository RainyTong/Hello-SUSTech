package com.example.hellosustechbackend.service;

import com.example.hellosustechbackend.api.ClassRepository;
import com.example.hellosustechbackend.api.UserRepository;
import com.example.hellosustechbackend.domain.User;
import com.example.hellosustechbackend.domain.Class;
import com.example.hellosustechbackend.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * This service will handle all the user interactions
 */
@RestController
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    /**
     * This method will register a user
     * @param user the user that will be registered
     * @return  {@code Status} with argument {@code true} if register
     *          successfully, {@code false} otherwise
     *
     */
    public Status register(User user) {
        Status status = new Status();
        if (!hasUser(user)) {
            if (user.getPassword().length() < 8) {
                status.setResult(false);
                status.setDescription("Error: Password must at least 8 characters");
                return status;
            }
            userRepository.save(user);
            status.setResult(true);
            status.setDescription("Register success");
        } else {
            status.setResult(false);
            status.setDescription("Error: Register fail");
        }
        return status;
    }

    /**
     * Let the user login
     * @param user the user requesting to login
     * @return  {@code Status} with argument {@code true} if login
     *          successfully, {@code false} otherwise
     *
     */
    public Status login(User user) {
        return comparePassword(user);
    }

    /**
     * Check whether the user exists
     * @param user user that will be checked
     * @return  {@code true} if exits, {@code false} otherwise
     *
     */
    private boolean hasUser(User user) {
        boolean existUsername = false;
        boolean existMailAddress = false;

        if (userRepository.findUserByUsername(user.getUsername()).size() > 0) {
            existUsername = true;
        }
        if (userRepository.findUserByMailAddress(user.getMailAddress()).size() > 0) {
            existMailAddress = true;
        }

        return existMailAddress || existUsername;
    }

    /**
     * Check whether the password is right
     * @param user user that will be checked
     * @return  {@code true} if it is correct, {@code false} otherwise
     *
     */
    private Status comparePassword(User user) {
        Status status = new Status();
        List<User> candidates = userRepository.findUserByUsername(user.getUsername());
        if(candidates.size()==0) {
            candidates = userRepository.findUserByMailAddress(user.getMailAddress());
            if(candidates.size()==0) {
                System.out.println(user.getMailAddress() + " has no corresponding candidate");
                status.setResult(false);
                status.setDescription("Error: No User");
                return status;
            }
        }
        User candidate = candidates.get(0);
        if (candidate.getPassword().equals(user.getPassword())) {
            status.setResult(true);
            status.setDescription(candidate.getUsername());
        } else {
            status.setResult(false);
            status.setDescription("Error: Wrong password");
        }
        return status;
    }

    /**
     *  Get a user's class table
     * @param username username
     * @return {@code Set} containing user's classes
     */
    public Set<Class> getClassTable(String username){
        User user = null;
        try {
            user = userRepository.findUserByUsername(username).get(0);
            return user.getClassSet();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Modify a user's class table
     * @param username username
     * @param class_id the id of a class
     * @param mode the operation type
     * @return {@code Status} whether the operation is valid
     */
    public Status modifyClassTable(String username,int class_id,int mode){
        User user;
        Class cls;
        Status status = new Status();
        try {
            List<User> users = userRepository.findUserByUsername(username);
            if(users.isEmpty())
                throw new Exception("No such user");
            else
                user = users.get(0);
            cls = classRepository.findById(class_id);
            if (cls == null)
                throw new Exception("No such class");
            if (mode == 0)
                user.getClassSet().add(cls);
            else if (mode == 1)
                user.getClassSet().remove(cls);
            else
                throw new Exception("Wrong operation mode");
            userRepository.save(user);
            status.setResult(true);
            status.setDescription("Successful");
        }catch (Exception e){
            status.setDescription(e.getMessage());
        }
        return status;
    }

}
