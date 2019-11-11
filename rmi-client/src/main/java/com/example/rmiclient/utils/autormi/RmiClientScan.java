package com.example.rmiclient.utils.autormi;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import(RmiClientScannerRegistrar.class)
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RmiClientScan {

    /**
     * 定义默认单值参数 basePackge
     * eg：@RmiClientScan("...")
     */
    String[] value() default {};

    /**
     * 定义记名参数 basePackge
     * eg：@RmiClientScan(basePackge="...")
     */
    String[] basePackages() default {};

    /**
     * 定义记名参数 basePackageClasses
     * eg：@RmiClientScan(basePackageClasses="...")
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * 定义记名参数 url
     * eg：@RmiClientScan(url="...")
     */
    String url() default "localhost:8080";

    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    Class<? extends Annotation> annotationClass() default Annotation.class;

    Class<? extends RmiClientFactory> factoryBean() default RmiClientFactory.class;
}
