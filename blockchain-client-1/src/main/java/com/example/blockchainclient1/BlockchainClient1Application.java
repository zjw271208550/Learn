package com.example.blockchainclient1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.example.blockchainclient1.mapper","com.example.blockchaincore.rdb.mapper"})
public class BlockchainClient1Application {

    public static void main(String[] args) {
        SpringApplication.run(BlockchainClient1Application.class, args);
    }

}
