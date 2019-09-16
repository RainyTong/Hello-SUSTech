package com.example.hellosustechbackend.api;

import com.example.hellosustechbackend.domain.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * Define the interface interact with the database
 */
public interface ClassRepository extends JpaRepository<Class, Long> {
    /**
     * Get classes from the database by weekday and lecture
     *
     * @param weekday the weekday
     * @param lecture the lecture time
     * @return {@code List} object that storing all found {@code Class} objects
     */
    List<Class> findClassByWeekdayAndLecture(int weekday, int lecture);

    /**
     * Get classes from the database by ID
     *
     * @param id the id of a class
     * @return {@code Class} object that have the correct id
     */
    Class findById(int id);
}
