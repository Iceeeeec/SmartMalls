package com.hsasys;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFileStorage
@SpringBootApplication
@EnableTransactionManagement
public class HsaSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(HsaSysApplication.class, args);
    }

}
