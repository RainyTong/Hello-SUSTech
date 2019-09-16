package com.example.hellosustechbackend.service;

import com.example.hellosustechbackend.api.MailAddressRepository;
import com.example.hellosustechbackend.domain.MailAddress;
import com.example.hellosustechbackend.mail.Mail;
import com.example.hellosustechbackend.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;


/**
 * This service will handle all the mail address interactions
 */
@RestController
public class MailAddressService {

    @Autowired
    private MailAddressRepository mailAddressRepository;

    /**
     * Send a mail containing a check code.
     *
     * @param mailAddress the mail address of the receiver
     *
     * @return  return a {@code Status} object with an argument
     *          {@code true} if send mail successfully,
     *          {@code false} otherwise.
     */
    public Status sendCheckCode(String mailAddress) {
        Status status = new Status();
        List<MailAddress> lists = mailAddressRepository.findByMailAddress(mailAddress);
        String checkCode = createCheckCode();

        if (lists.size() == 0) {
            MailAddress m = new MailAddress();
            m.setCheckCode(checkCode);
            m.setMailAddress(mailAddress);
            mailAddressRepository.save(m);
        } else if (lists.size() == 1) {
            MailAddress m = lists.get(0);
            m.setCheckCode(checkCode);
            mailAddressRepository.save(m);
        } else {
            status.setResult(false);
            status.setDescription("Error: Duplicate mail address");
            return status;
        }
        //send email
        boolean result;
        try {
            Mail mail = new Mail(mailAddress, checkCode);
            result = mail.SendMessage("Hello SUSTCech Check Code","Check code: ");
        } catch (Exception e) {
            result = false;
        }
        status.setResult(result);
        if (result) {
            status.setDescription("The check code is successfully send to your mail box");
        } else {
            status.setDescription("The check code cannot send to your mail box");
        }
        return status;
    }

    /**
     * Verify whether the check code is correct
     *
     * @param mailAddress the mail address from the client
     * @param checkCode the check code from the client
     *
     * @return  {@code Status} object with an argument {@code true}
     *          if the check code is right, {@code false} otherwise.
     */
    public Status verifyCheckCode(String mailAddress, String checkCode) {
        Status status = new Status();
        List<MailAddress> lists = mailAddressRepository.findByMailAddress(mailAddress);
        if (lists.size() == 1) {
            MailAddress m = lists.get(0);
            if (m.getCheckCode().equals(checkCode)) {
                status.setResult(true);
                status.setDescription("Correct check code");
            } else {
                status.setResult(false);
                status.setDescription("Error: Wrong check code");
            }
            return status;
        }
        status.setResult(false);
        status.setDescription("Error: Duplicate mail address");
        return status;
    }

    private String createCheckCode(){
        Random random = new Random();
        int result = 0;
        for (int i = 0; i < 6; i++) {
            result += result * 10 + random.nextInt(10);
        }
        return String.valueOf(result);
    }
}
