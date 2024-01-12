package com.github.stateless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableFeignClients
@SpringBootApplication
public class StatelessApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatelessApplication.class, args);
    }

}
