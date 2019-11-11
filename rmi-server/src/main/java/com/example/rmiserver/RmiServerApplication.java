package com.example.rmiserver;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Server 启动类
 */
@SpringBootApplication
public class RmiServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RmiServerApplication.class)
                .web(WebApplicationType.NONE) // 设置为非 WEB
                .run(args);
    }

}
