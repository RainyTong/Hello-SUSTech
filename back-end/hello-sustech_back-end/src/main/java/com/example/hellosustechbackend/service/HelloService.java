package com.example.hellosustechbackend.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The hello web page of the server. It is to check
 * the running status of the server.
 * */
@RestController
@RequestMapping("/hello")
public class HelloService {

    /**
     * Generating the hello page
     *
     * @return {@code String} whose contents are "Hello world!"
     */
    @GetMapping
    public String hello() {
        return "Hello world";
    }
}