package com.example.hellosustechbackend.api;

import java.util.List;

import com.example.hellosustechbackend.domain.MailAddress;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Store the methods to do mail-related operations on mysql database
 */
public interface MailAddressRepository extends JpaRepository<MailAddress, Long> {

    /**
     * Get mailaddress-checkcode pairs from the database by mail address
     *
     * @param mailAddress the mail address
     * @return {@code List} object that storing all found {@code MailAddress} objects
     */
    List<MailAddress> findByMailAddress(String mailAddress);
}
