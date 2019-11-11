package com.example.rmiserver.core;


import com.example.rmiserver.core.logic.InfoLogic;
import com.example.rmiserver.service.InfoRMI;
import com.example.rmiserver.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

/**
 * Server 配置类
 */
@Configuration
@ComponentScan("com.example.rmiserver")
public class Configure {

    @Autowired
    private InfoService infoService;

    //加入 RmiServiceExporter并把接口及接口实现类注册
    @Bean
    public RmiServiceExporter rmiServiceExporter(){
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceInterface(InfoRMI.class);
        exporter.setServiceName("InfoRMI");
        exporter.setService(infoService);
        exporter.setServicePort(6789);
        exporter.setRegistryPort(1234);
        return exporter;
    }

    //注册逻辑类
    @Bean
    @ConditionalOnClass(InfoLogic.class)
    public InfoLogic infoLogic() {
        return new InfoLogic();
    }
}
