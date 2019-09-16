package com.example.hellosustechbackend.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import java.util.Set;

@Entity
public class Test2 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany(mappedBy = "test2Set")
    private Set<Test> testSet;

    public Test2() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Test> getClassSet() {
        return testSet;
    }
    
    public void setClassSet(Set<Test> classSet) {
        this.testSet = classSet;
    }
}
