package com.example.blockchainclient1.controller;

import com.example.blockchainclient1.bean.Data;
import com.example.blockchainclient1.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
public class Controller {

    @Autowired
    BaseService service;

    /**
     * 初始化区块链的创世块
     * ===> Block
     * ===> 是否创世 >> 是否合法
     */
    @RequestMapping("/init")
    public String init(){
        service.init();
        return "OK";
    }

    /**
     * 添加一条数据
     * ===> 原始数据与 BC的关系
     * ===> Tx机制
     * ===> 检查数据唯一性 >> 创建 txIn、txOut >> 创建tx >> 签名 >> Tx信息入库 >> data入库
     */
    @RequestMapping("/add")
    public String add(){
        Data data = new Data("abcd1234","xxxxxx",null,new Date(new java.util.Date().getTime()));
        try {
            service.addOneData(data);
            return "OK";
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 挖掘区块，正式上链
     * ===> UTxOut
     * ===> 获取未上链 Tx（并广播） >> 验证 Tx合法性 >> 验证 TxIn合法性 >> 生成区块（并广播） >> 生成 UTxOut(更新 Tx)
     */
    @RequestMapping("/mine")
    public String mine(){
        try {
            service.mine();
            return "OK";
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 在整个网络中查询（不过滤地址公钥）
     * ===> UTxOut中是否存在 >> 是否一致
     */
    @RequestMapping("/quaryall")
    public Data quaryInAll(){
        String bh = "abcd1234";
        Data data = service.getDataById(bh);
        return data;
    }

    /**
     * 结合全网情况检查篡改。
     */
    // @RequestMapping("/check")
    // public String check(){
    //
    // }

}
