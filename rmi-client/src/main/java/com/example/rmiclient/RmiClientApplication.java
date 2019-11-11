package com.example.rmiclient;

import com.example.rmiclient.utils.autormi.RmiClientScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Client启动类
 */
@SpringBootApplication
@RmiClientScan(value = "com.example.rmiclient.service",url = "localhost:1234")
public class RmiClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RmiClientApplication.class, args);
    }

}
