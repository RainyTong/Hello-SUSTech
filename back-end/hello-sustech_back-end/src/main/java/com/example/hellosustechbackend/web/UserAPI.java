package com.example.hellosustechbackend.web;

import com.example.hellosustechbackend.domain.Class;
import com.example.hellosustechbackend.domain.User;
import com.example.hellosustechbackend.service.UserService;
import com.example.hellosustechbackend.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserAPI {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Status register(User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public Status logIn(User user) {
        return userService.login(user);
    }

    @GetMapping("/class_table")
    public Set<Class> getClassTable(@RequestParam String username){
        return userService.getClassTable(username);
    }

    @PostMapping("/class_table")
    public Status addClassTable(String username,int class_id,int mode){
        System.err.println("class id : "+class_id);
            return userService.modifyClassTable(username,class_id,mode);
    }

}
