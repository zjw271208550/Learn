package com.example.rmiserver.service;

import com.example.rmiserver.core.logic.InfoLogic;
import com.example.rmiserver.entity.Info;
import com.example.rmiserver.mapper.InfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Server服务类同时也是远程接口实现类
 */
@Service
public class InfoService implements InfoRMI{

    @Autowired
    InfoMapper infoMapper;
    @Autowired
    InfoLogic infoLogic;

    /**
     * 实现远程接口
     * @param id info对象ID
     * @param value info对象值
     * @throws RemoteException
     */
    @Override
    public void updateInfoValueById(String id,Integer value) throws RemoteException {
        infoLogic.updateValueById(id,value);
        infoMapper.setInfoValueById(value,id);
    }

    /**
     * 初始化逻辑类内数据对象启动逻辑线程
     */
    public void initIdList(){
        List<Info> infoList = infoMapper.getInfos();
        infoLogic.initIdList(infoList);
        infoLogic.start();
    }

}
