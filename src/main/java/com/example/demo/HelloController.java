package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
