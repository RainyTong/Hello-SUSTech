package com.example.hellosustechbackend.service;

import com.example.hellosustechbackend.api.ClassRepository;
import com.example.hellosustechbackend.domain.Class;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This service will handle all class interactions
 */
@RestController
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    /**
     * This method will return the all the classes according to the specific {@code weekday}and {@code classseq}
     *
     * @param weekday the weekday
     * @param lecture the sequence number of the class
     *
     * @return {@code List<Class>} all the classes
     */
    public List<Class> getClasses(int weekday, int lecture){
        return classRepository.findClassByWeekdayAndLecture(weekday,lecture);
    }
}
