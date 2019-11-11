package com.example.rmiclient.utils.autormi;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * 扫描器，通过doScan方法将basePackages中的class，扫描进spring容器中
 */
public class RmiClientScanner extends ClassPathBeanDefinitionScanner {

    private RmiClientFactory rmiClientFactory = new RmiClientFactory<Object>();

    private Class<?> markerInterface;

    private Class<? extends Annotation> annotationClass;

    private String url;

    //构造器 —— 默认方法
    public RmiClientScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }
    //设置注解类 —— 默认方法
    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    //设置 url
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 配置父扫描程序以搜索正确的界面。
     * 它可以搜索所有接口，也可以仅搜索继承 markerInterface的接口 或/和 带有注解类注释的接口。
     */
    public void registerFilters() {
        boolean acceptAllInterfaces = true;

        // 如果指定了，就使用给定的 annotation 和/或 markerInterface
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        // 如果指定了，重写 AssignableTypeFilter 以忽略实际 markerInterface 上的匹配项
        if (this.markerInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
                @Override
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }


        if (acceptAllInterfaces) {
            // 引入默认过滤器和所有类
            addIncludeFilter(new TypeFilter() {
                public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                    return true;
                }
            });
        }

        // 排出 package-info.java
        addExcludeFilter(new TypeFilter() {
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                String className = metadataReader.getClassMetadata().getClassName();
                return className.endsWith("package-info");
            }
        });
    }

    /**
     * 入口
     * 调用父级搜索，该搜索将搜索并注册所有候选者。
     * 然后对注册的对象进行后期处理以将其设置为MapperFactoryBeans
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("No Rmi API was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    /**
     * 主要方法
     * @param beanDefinitions
     */
    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("Creating ProxyFactoryBean with name '" + holder.getBeanName()
                        + "' and '" + definition.getBeanClassName() + "' RmiInterface");
            }

            // Rmi 接口是原生Bean类
            // 但实际 Bean的类是 ProxyFactoryBean
            //向 ProxyFactoryBean的构造器注入参数
            definition.getConstructorArgumentValues().addGenericArgumentValue(this.url);
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.setBeanClass(this.rmiClientFactory.getClass());

            if (logger.isDebugEnabled()) {
                logger.debug("Enabling autowire by type for ProxyFactoryBean with name '" + holder.getBeanName() + "'.");
            }
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            logger.warn("Skipping ProxyFactoryBean with name '" + beanName
                    + "' and '" + beanDefinition.getBeanClassName() + "' RmiInterface"
                    + ". Bean already defined with the same name!");
            return false;
        }
    }
}
