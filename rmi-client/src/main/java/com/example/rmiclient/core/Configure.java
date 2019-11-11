// package com.example.rmiclient.core;
//
// import com.example.rmiclient.service.InfoRMI;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.remoting.rmi.RmiProxyFactoryBean;
//
// /**
//  * Client 配置类
//  */
// @Configuration
// public class Configure {
//
//     //加入RmiProxyFactoryBean 代理
//     @Bean(name = "infoService")
//     public RmiProxyFactoryBean rmiProxyFactoryBean(){
//         RmiProxyFactoryBean rmiProxyFactoryBean=new RmiProxyFactoryBean();
//         rmiProxyFactoryBean.setServiceInterface(InfoRMI.class);
//         rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1234/infoService");
//         return rmiProxyFactoryBean;
//     }
// }
