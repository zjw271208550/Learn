package com.example.junit5;

import org.springframework.stereotype.Service;

@Service
public class Services {

    public String show(User user){
        return "hello - "+user.toString();
    }

    public int addUserValue(int val1,int val2){
        return val1+val2;
    }
}
