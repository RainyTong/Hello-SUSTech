package com.example.hellosustechbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Class {

    /** The primary key of this object in database.*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int lecture;

    @JsonIgnore
    @ManyToMany(mappedBy = "classSet")
    private Set<User> userSet = new HashSet<>();

    private String name;

    private String classroom;

    private String teachingweek;

    private String professor;

    private int weekday;


    /** The default constructor.*/
    public Class() {
    }

    public Class(String lecture,String name,String classroom,String professor,String teachingweek,String weekday){
        this.lecture = Integer.parseInt(lecture);
        this.name = name;
        this.classroom = classroom;
        this.teachingweek = teachingweek;
        this.professor = professor;
        this.weekday = Integer.parseInt(weekday);
    }

    public Class(int lecture,String name,String classroom,String professor,String teachingweek,int weekday){
        this.lecture = lecture;
        this.name = name;
        this.classroom = classroom;
        this.teachingweek = teachingweek;
        this.professor = professor;
        this.weekday = weekday;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    public int getLecture() {
        return lecture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLecture(int lecture) {
        this.lecture = lecture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTeachingweek() {
        return teachingweek;
    }

    public void setTeachingweek(String teachingweek) {
        this.teachingweek = teachingweek;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }
}
