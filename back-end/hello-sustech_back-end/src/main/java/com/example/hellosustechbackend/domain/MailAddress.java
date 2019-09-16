package com.example.hellosustechbackend.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Store the correspondence between the e-mail address and check codes.
 * */
@Entity
public class MailAddress {
    /**
     * The primary key of this object in database.
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int mailId;

    /** The mail address*/
    private String mailAddress;

    /** The corresponding check code*/
    private String checkCode;

    /** The default constructor.*/
    public MailAddress() {
    }

    public int getMailId() {
        return mailId;
    }

    public void setMailId(int mailId) {
        this.mailId = mailId;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
}
