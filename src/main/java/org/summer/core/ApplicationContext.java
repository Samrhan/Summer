package org.summer.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationContext {

    private final BeanContainer beanContainer;
    private final PackageScanner packageScanner;
    private final DependencyGraph<Class<?>> beanDependencyGraph;

    public ApplicationContext() {
        this.beanContainer = new BeanContainer();
        this.packageScanner = new PackageScanner();
        this.beanDependencyGraph = new BeanDependencyGraph();
    }

    public void initialize(String basePackage) throws IOException, ClassNotFoundException, ReflectiveOperationException {
        List<Class<?>> beanClasses = scanForBeanClasses(basePackage);
        buildDependencyGraph(beanClasses);
        List<Class<?>> sortedBeans = beanDependencyGraph.sort();
        instantiateAndRegisterBeans(sortedBeans);
    }

    private List<Class<?>> scanForBeanClasses(String basePackage) throws IOException, ClassNotFoundException {
        return packageScanner.scanPackageForBeans(basePackage);
    }

    private void buildDependencyGraph(List<Class<?>> beanClasses) {
        for (Class<?> cls : beanClasses) {
            beanDependencyGraph.addElement(cls);
        }
    }

    private void instantiateAndRegisterBeans(List<Class<?>> sortedBean) throws ReflectiveOperationException {
        for (Class<?> clazz : sortedBean) {
            Object beanInstance = instantiateBean(clazz);
            beanContainer.registerBean(clazz, beanInstance);
        }
    }

    private Object instantiateBean(Class<?> clazz) throws ReflectiveOperationException {
        Constructor<?> constructor = clazz.getConstructors()[0];
        List<Object> dependencies = Arrays.stream(constructor.getParameterTypes())
                .map(this::resolveDependency)
                .collect(Collectors.toList());
        return constructor.newInstance(dependencies.toArray());
    }

    private Object resolveDependency(Class<?> dependencyClass) {
        return beanContainer.getBean(dependencyClass);
    }
}