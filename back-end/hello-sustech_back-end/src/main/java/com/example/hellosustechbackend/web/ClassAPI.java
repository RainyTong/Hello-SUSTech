package com.example.hellosustechbackend.web;

import com.example.hellosustechbackend.domain.Class;
import com.example.hellosustechbackend.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassAPI {

    @Autowired
    private ClassService classService;

    /**
     * This method will return the all the classes according to the specific {@code weekday}and {@code classseq}
     *
     * @param weekday the weekday
     * @param lecture the sequence number of the class
     *
     * @return {@code List<Class>} all the classes according to the inputs
     */
    @GetMapping("/get")
    public List<Class> viewClasses(int weekday,int lecture){
        return classService.getClasses(weekday,lecture);
    }
}