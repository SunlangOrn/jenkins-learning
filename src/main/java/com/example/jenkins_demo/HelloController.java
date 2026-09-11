package com.example.jenkins_demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello Automated With Jenkins CI/CD!";
    }

    @GetMapping("/health")
    public String health() {
        throw new RuntimeException("Simulated runtime failure!");
    }
}