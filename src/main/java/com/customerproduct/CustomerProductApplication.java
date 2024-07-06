package com.customerproduct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.customerproduct.*")
@EntityScan("com.customerproduct.*")
public class CustomerProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerProductApplication.class, args);
    }

}
