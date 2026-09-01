package com.example.jenkins_demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/helo")
    public String sayHello(){
        return  "Hello Jenkins CI/CD!";
    }
}
