package com.example.blockchainclient1.service;

import com.example.blockchainclient1.mapper.DBMapper;
import com.example.blockchainclient1.bean.Data;
import com.example.blockchainclient1.util.ClientKey;
import com.example.blockchaincore.rdb.func.ServiceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService {
    private final Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    private DBMapper dbMapper;

    @Autowired
    private ClientKey clientKey;

    public Data getDataById(String id){
        Data data = dbMapper.getDataById(id);
        if(checkDataById(data)){
            return data;
        }else{
            logger.error("账目中这个编号的数据与数据库中的不符");
            return null;
        }
    }

    public void init(){
        if(!ServiceFunction.hasInit(dbMapper)) {
            ServiceFunction.initBlock(dbMapper);
        }
    }

    public boolean addOneData(Data data) throws Exception{
        if(checkDataById(data)){
            logger.error("账目中已存在这个编号的数据");
            return false;
        }else {
            ServiceFunction.addNewData(dbMapper,clientKey.getPublicKey(),clientKey.getPrivateKey(),data);
            dbMapper.addData(data);
            return true;
        }
    }

    public boolean mine() throws Exception{
        return ServiceFunction.mineTransactions(dbMapper,clientKey.getPublicKey());
    }


    private boolean checkDataById(Data dataInDB){
        return ServiceFunction.checkDataById(dbMapper,dataInDB);
    }
}
