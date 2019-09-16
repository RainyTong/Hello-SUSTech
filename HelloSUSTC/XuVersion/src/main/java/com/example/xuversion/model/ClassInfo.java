package com.example.xuversion.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ClassInfo extends RealmObject {

    @PrimaryKey
    private long id;

    private int lecture;
    private int weekday;
    private String name;
    private String classroom;
    private String professor;
    private String teachingweek;

    /**
     * empty constructor
     */
    public ClassInfo(){

    }

    /**
     * Constructor for ClassInfo with two parameter
     * @param lecture the lecture number
     * @param weekday the weekday number
     */
    public ClassInfo(int lecture, int weekday){
        this.lecture = lecture;
        this.weekday = weekday;
    }

    /**
     * Constructor for ClassInfo with five parameter
     * @param id the class id
     * @param lecture the lecture name
     * @param weekday weekday name
     * @param name name
     * @param classroom classroom
     * @param professor professor
     * @param teachingweek teachingweek
     */
    public ClassInfo(long id, int lecture, int weekday, String name, String classroom, String professor, String teachingweek) {
        this.id = id;
        this.lecture = lecture;
        this.weekday = weekday;
        this.name = name;
        this.classroom = classroom;
        this.professor = professor;
        this.teachingweek = teachingweek;
    }

    /**
     * ID Getter
     * @return the id of the class
     */
    public long getId() {
        return id;
    }

    /**
     * ID setter
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * if a name is a result
     * @param tempName the searched name
     * @return true or false
     */
    public boolean isAResult(String tempName){
        if(name.contains(tempName) || professor.contains(tempName) || classroom.contains(tempName)){
            return true;
        }
        return false;
    }

    /**
     * TeachingWeek getter
     * @return teaching week
     */
    public String getTeachingweek() {
        return teachingweek;
    }

    /**
     * Teaching week setter
     * @param teachingweek the teaching week
     */
    public void setTeachingweek(String teachingweek) {
        this.teachingweek = teachingweek;
    }

    /**
     * Lecture getter
     * @return the lecture
     */
    public int getLecture() {
        return lecture;
    }


    /**
     * Weekday getter
     * @return the weekday
     */
    public int getWeekday() {
        return weekday;
    }


    /**
     * name getter
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * name setter
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * override toString
     * @return string representation
     */
    @Override
    public String toString() {
        return id + name + professor +classroom+ teachingweek;
    }

    /**
     * Get class room
     * @return classroom
     */
    public String getClassroom() {
        return classroom;
    }

    /**
     * classroom setter
     * @param classroom set class room
     */
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    /**
     * professor getter
     * @return get professor
     */
    public String getProfessor() {
        return professor;
    }

    /**
     * professor setter
     * @param professor set professor
     */
    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
