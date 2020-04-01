package com.yang.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class YangbbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YangbbsApplication.class, args);
    }

}
