package com.iflytek.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.iflytek.web.mapper")
@EnableTransactionManagement
public class PersonalizedApplication {
    public static void main(String[] args) {
        SpringApplication.run(PersonalizedApplication.class, args);
    }
}
