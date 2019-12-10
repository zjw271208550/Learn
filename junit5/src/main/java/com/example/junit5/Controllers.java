package com.example.junit5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Controllers {

    @Autowired
    Services services;

    @RequestMapping("/show")
    public String show(String id,String name,int value){
        return services.show(new User(id,name,value));
    }
}
