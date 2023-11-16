package org.summer.core;

import org.summer.core.bean.BeanContainer;
import org.summer.core.bean.BeanFactory;
import org.summer.core.bean.BeanScanner;
import org.summer.core.bean.BeanDependencyGraph;
import org.summer.core.util.DependencyResolver;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ApplicationContext {

    private final BeanScanner beanScanner;
    private final BeanFactory beanFactory;
    private final DependencyResolver dependencyResolver;

    public ApplicationContext() {
        BeanContainer beanContainer = new BeanContainer();

        this.beanFactory = new BeanFactory(beanContainer);
        this.beanScanner = new BeanScanner();

        this.dependencyResolver = new DependencyResolver();
    }

    public void initialize(String basePackage) throws IOException, ReflectiveOperationException {
        Set<Class<?>> classes = beanScanner.scanForBeanClasses(basePackage);
        List<Class<?>> sortedBeans = orderDependencies(classes);

        beanFactory.instantiateAndRegisterBeans(sortedBeans);
    }

    private List<Class<?>> orderDependencies(Set<Class<?>> classes){
        BeanDependencyGraph beanDependencyGraph = new BeanDependencyGraph();

        for (Class<?> clazz : classes) {
            List<Class<?>> dependencies = dependencyResolver.getDependenciesForClass(clazz);
            beanDependencyGraph.addElement(clazz, dependencies);
        }

        return beanDependencyGraph.build();
    }
}