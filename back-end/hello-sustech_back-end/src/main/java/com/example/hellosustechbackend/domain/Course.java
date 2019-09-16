package com.example.hellosustechbackend.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int classSeq;
    private int classSeq2;
    private String classname;
    private String lecturer;
    private String venue;
    private String weektimes;
    private String weekday;

    public Course() {
    }

    public Course(int id, int classSeq, int classSeq2, String classname, String lecturer, String venue, String weektimes, String weekday) {
        this.id = id;
        this.classSeq = classSeq;
        this.classSeq2 = classSeq2;
        this.classname = classname;
        this.lecturer = lecturer;
        this.venue = venue;
        this.weektimes = weektimes;
        this.weekday = weekday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassSeq() {
        return classSeq;
    }

    public void setClassSeq(int classSeq) {
        this.classSeq = classSeq;
    }

    public int getClassSeq2() {
        return classSeq2;
    }

    public void setClassSeq2(int classSeq2) {
        this.classSeq2 = classSeq2;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getWeektimes() {
        return weektimes;
    }

    public void setWeektimes(String weektimes) {
        this.weektimes = weektimes;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }
}
