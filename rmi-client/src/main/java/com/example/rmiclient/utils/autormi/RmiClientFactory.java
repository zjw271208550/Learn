package com.example.rmiclient.utils.autormi;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * 自定义的代理工厂，用于将指定的接口转换为代理实体
 */
public class RmiClientFactory<T> implements FactoryBean<T>, InitializingBean {
    private Class<T> interfaceClass;
    private String url = "localhost:8080";

    public RmiClientFactory() {
    }

    public RmiClientFactory(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
    public RmiClientFactory(String url, Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
        this.url = url;
    }

    public T createBean(){
        RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
        bean.setServiceUrl("rmi://"+url+"/"+interfaceClass.getSimpleName());
        bean.setServiceInterface(interfaceClass);
        bean.afterPropertiesSet();
        return (T)bean.getObject();
    }

    @Override
    public T getObject() throws Exception {
        return createBean();
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("AfterPropertiesSet");
    }

}
