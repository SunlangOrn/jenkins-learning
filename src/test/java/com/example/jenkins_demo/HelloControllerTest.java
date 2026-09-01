package com.example.jenkins_demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloControllerTest {

    @Test
    public void testSayHello() {
        HelloController controller = new HelloController();
        assertEquals("Hello Jenkins CI/CD!", controller.sayHello());
    }
}
