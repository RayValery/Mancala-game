package com.test.mancalagame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class MancalaGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(MancalaGameApplication.class, args);
    }

}
