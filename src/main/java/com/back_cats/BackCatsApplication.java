package com.back_cats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(scanBasePackages = {"com.back_cats"})
public class BackCatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackCatsApplication.class, args);
    }
}
