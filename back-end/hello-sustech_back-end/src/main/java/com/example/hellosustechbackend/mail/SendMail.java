package com.example.hellosustechbackend.mail;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * Define the account generating and sending mails
 * containing a check code automatically
 * */
public class SendMail {
    private String username;
    private String password;
    private MimeMessage mimeMessage =null;
    private Properties pros = null;
    private Multipart multipart = null;

    SendMail(String username, String password) {
        this.username = username;
        this.password = password;
    }

    void initMessage() {
        Authenticator auth = new EmailAuthenticator();
        Session session = Session.getDefaultInstance(pros, auth);
        session.setDebug(true);
        mimeMessage = new MimeMessage(session);
    }

    void setPros(Properties pros) {
        this.pros = pros;
    }

    void setSubject(String subject) throws MessagingException {
        mimeMessage.setSubject(subject);
    }

    void setDate(Date date) throws MessagingException {
        mimeMessage.setSentDate(date);
    }

    void setRecipient(String recipient) throws MessagingException {
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
    }

    void setFrom(String from) throws UnsupportedEncodingException, MessagingException {
        mimeMessage.setFrom(new InternetAddress(username, from));
    }

    /**
     * Send a message to the target address
     * @return  {@code true} if send it successfully,
     *          {@code false} otherwise.
     * */
    boolean sendMessage() {
        try {
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

    void setContent(String s, String type) throws MessagingException {
        if (multipart == null) {
            multipart = new MimeMultipart();
        }
        BodyPart bodypart = new MimeBodyPart();
        bodypart.setContent(s, type);
        multipart.addBodyPart(bodypart);
        mimeMessage.setContent(multipart);
        mimeMessage.saveChanges();
    }

    /**
     * Login the authorized account, which is used to send
     * check codes.
     * */
    public class EmailAuthenticator extends Authenticator
    {
        /**
         * Get a permission from the mail-server.
         *
         * @return  A {@code PasswordAuthentication} object containing
         *          the permission
         * */
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username, password);
        }
    }
}
