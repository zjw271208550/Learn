package com.example.rmiclient.utils.autormi;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RmiClientScannerRegistrar implements ImportBeanDefinitionRegistrar {

    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //获取注解 RmiClientScan 的所有属性字段
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(RmiClientScan.class.getName())
        );
        //定义扫描器
        RmiClientScanner scanner = new RmiClientScanner(registry);
        //给扫描器设置资源加载器
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }
        //给扫描器设置 Bean名称生成器
        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
        }

        //获取 value数组并将值加入 basePackages
        List<String> basePackages = new ArrayList<String>();
        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        //获取 url字符串
        String url = annoAttrs.getString("url");

        //获取 basePackages数组并将值加入 basePackages
        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        //获取 basePackageClasses数组并将值对应的PackageName加入 basePackages
        for (Class<?> clazz : annoAttrs.getClassArray("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        scanner.registerFilters();
        scanner.setUrl(url);
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }
}
