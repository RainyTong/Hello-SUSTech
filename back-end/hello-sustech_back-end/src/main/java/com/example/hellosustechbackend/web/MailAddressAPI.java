package com.example.hellosustechbackend.web;

import com.example.hellosustechbackend.service.MailAddressService;
import com.example.hellosustechbackend.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * The mail address api
 */
@RestController
@RequestMapping("/checkcode")
public class MailAddressAPI {

    @Autowired
    private MailAddressService mailAddressService;

    /**
     * Send a check code to the receiver
     *
     * @param mailAddress the uploaded mail address from the
     *                    client.
     *
     * @return  return a {@code Status} object with an argument
     *          {@code true} if send mail successfully,
     *          {@code false} otherwise.
     */
    @PostMapping("/get")
    public Status sendCheckCode(@RequestParam String mailAddress) {
        return mailAddressService.sendCheckCode(mailAddress);
    }

    /**
     * Send a check code to the receiver
     *
     * @param mailAddress the uploaded mail address from the
     *                    client.
     * @param checkCode the check code form the client
     *
     * @return  return a {@code Status} object with an argument
     *          {@code true} if the check code is right,
     *          {@code false} otherwise.
     */
    @PostMapping("/verify")
    public Status verifyCheckCode(@RequestParam String mailAddress, @RequestParam String checkCode) {
        return mailAddressService.verifyCheckCode(mailAddress, checkCode);
    }

}
