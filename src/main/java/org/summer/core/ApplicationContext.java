package org.summer.core;

import org.summer.core.bean.BeanContainer;
import org.summer.core.bean.BeanFactory;
import org.summer.core.bean.BeanScanner;
import org.summer.core.bean.BeanDependencyGraph;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.summer.core.util.DependencyResolver.getDependenciesForClass;

public class ApplicationContext {

    private final BeanContainer beanContainer;

    public ApplicationContext() {
        beanContainer = new BeanContainer();
    }

    public void initialize(String basePackage) throws IOException {
        List<Class<?>> beans = scanBeans(basePackage);
        registerBeans(beans);
    }

    private void registerBeans(List<Class<?>> sortedBeans) {
        BeanFactory beanFactory = new BeanFactory(beanContainer::getBean);

        for(Class<?> clazz: sortedBeans) {
            Object bean = beanFactory.createBean(clazz);
            beanContainer.registerBean(clazz, bean);
        }
    }

    private List<Class<?>> scanBeans(String basePackage) throws IOException {
        BeanScanner beanScanner = new BeanScanner();
        Set<Class<?>> classes = beanScanner.scanForBeanClasses(basePackage);
        return orderDependencies(classes);
    }

    private List<Class<?>> orderDependencies(Set<Class<?>> classes){
        BeanDependencyGraph beanDependencyGraph = new BeanDependencyGraph();

        for (Class<?> clazz : classes) {
            List<Class<?>> dependencies = getDependenciesForClass(clazz);
            beanDependencyGraph.addElement(clazz, dependencies);
        }

        return beanDependencyGraph.build();
    }
}