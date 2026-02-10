package com.team.jpquiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JpQuizApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpQuizApplication.class, args);
    }
}
