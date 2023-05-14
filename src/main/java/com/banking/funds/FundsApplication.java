package com.banking.funds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class FundsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundsApplication.class, args);
    }

}
