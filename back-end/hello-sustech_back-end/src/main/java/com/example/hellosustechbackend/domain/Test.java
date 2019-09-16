package com.example.hellosustechbackend.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "TEST_TEST2", joinColumns =
    @JoinColumn(name = "TEST_ID"), inverseJoinColumns =
    @JoinColumn(name = "TEST2_ID"))
    private Set<Test2> test2Set;

    public Test() {
        this.test2Set = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Test2> getClassSet() {
        return test2Set;
    }

    public void setClassSet(Set<Test2> test2Set) {
        this.test2Set = test2Set;
    }
}
