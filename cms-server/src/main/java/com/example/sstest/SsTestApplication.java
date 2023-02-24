package com.example.sstest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.sstest.mapper")
public class SsTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsTestApplication.class, args);
    }

}
