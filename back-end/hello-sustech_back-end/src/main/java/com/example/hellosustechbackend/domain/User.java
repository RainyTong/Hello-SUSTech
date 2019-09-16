package com.example.hellosustechbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

/**
 * User object
 */
@Entity
public class User {

    /**
     * The primary key of this object in database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The mail address of the user
     */
    private String mailAddress;

    /**
     * The username of the user
     */
    private String username;

    /**
     * The password of the user
     */
    private String password;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "USER_CLASS", joinColumns =
    @JoinColumn(name = "USER_ID"), inverseJoinColumns =
    @JoinColumn(name = "CLASS_ID"))
    private Set<Class> classSet = new HashSet<>();

    /**
     * The default constructor.
     */
    public User() {
    }

    /**
     * Creates a new user
     *
     * @param mailAddress email address of the user
     * @param username    unique username of the user
     * @param password    password of the user
     */
    public User(String mailAddress, String username, String password) {
        this.mailAddress = mailAddress;
        this.username = username;
        this.password = password;
    }

    public Set<Class> getClassSet() {
        return classSet;
    }

    public void setClassSet(Set<Class> classSet) {
        this.classSet = classSet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
