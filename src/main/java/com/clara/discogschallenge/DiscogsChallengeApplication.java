package com.clara.discogschallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DiscogsChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscogsChallengeApplication.class, args);
    }

}
