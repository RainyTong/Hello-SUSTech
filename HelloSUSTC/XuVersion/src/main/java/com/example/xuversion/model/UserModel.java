package com.example.xuversion.model;

public class UserModel {
    private String name;
    private String mailbox;
    private String password;
    private String checkcode;

    /**
     * get check code
     * @return check code
     */
    public String getCheckcode() {
        return checkcode;
    }

    /**
     * set check code
     * @param checkcode check code
     */
    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    /**
     * get name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * set name
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get mail box
     * @return mail box
     */
    public String getMailbox() {
        return mailbox;
    }

    /**
     * set mail box
     * @param mailbox mail box
     */
    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    /**
     * get password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set pass word
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
