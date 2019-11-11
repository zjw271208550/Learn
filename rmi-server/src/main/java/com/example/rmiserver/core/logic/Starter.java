package com.example.rmiserver.core.logic;


import com.example.rmiserver.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 逻辑的启动类，在 Server启动后执行
 */
@Component
public class Starter implements CommandLineRunner {

    @Autowired
    InfoService infoService;

    @Override
    public void run(String... args) throws Exception {
        infoService.initIdList();
    }
}
