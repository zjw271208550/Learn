package com.example.rmiclient.controller;

import com.example.rmiclient.service.InfoRMI;
import com.example.rmiclient.utils.autormi.RmiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Client web controller
 */
@Controller
public class InfoController {

    @Autowired
    private InfoRMI infoClient;

    @ResponseBody
    @RequestMapping("/update/{id}/{value}")
    public String update(@PathVariable String id, @PathVariable Integer value) throws Exception{
        //使用远程接口
        // InfoRMI infoService = (InfoRMI)rmiBean.getObject();
        infoClient.updateInfoValueById(id,value);
        return "DONE";
    }
}
