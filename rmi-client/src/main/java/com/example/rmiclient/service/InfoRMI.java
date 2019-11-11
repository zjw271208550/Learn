package com.example.rmiclient.service;

import com.example.rmiclient.utils.autormi.RmiClient;

/**
 * Client方法接口定义，不需要实现类
 */
@RmiClient
public interface InfoRMI {
    public void updateInfoValueById(String id,Integer value);
}
