package org.summer.core;

import org.summer.core.annotation.Component;
import org.summer.core.dependency.BeanDependencyGraph;
import org.summer.core.dependency.DependencyGraph;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationContext {

    private final BeanContainer beanContainer;
    private final PackageScanner packageScanner;
    private final DependencyGraph<Class<?>> beanDependencyGraph;

    public ApplicationContext() {
        this.beanContainer = new BeanContainer();
        this.packageScanner = new PackageScanner();
        this.beanDependencyGraph = new BeanDependencyGraph();
    }

    public void initialize(String basePackage) throws IOException, ReflectiveOperationException {
        Set<Class<?>> classes = scanForBeanClasses(basePackage);
        buildDependencyGraph(classes);
        List<Class<?>> sortedBeans = beanDependencyGraph.build();
        instantiateAndRegisterBeans(sortedBeans);
    }

    private Set<Class<?>> scanForBeanClasses(String basePackage) throws IOException, ClassNotFoundException {
        Stream<Class<?>> classes = packageScanner.scanPackages(basePackage);
        return filterComponentClasses(classes);
    }

    private Set<Class<?>> filterComponentClasses(Stream<Class<?>> classes) {
        return classes
                .filter(this::isComponentClass)
                .collect(Collectors.toSet());
    }

    private boolean isComponentClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }

    private void buildDependencyGraph(Set<Class<?>> beanClasses) {
        for (Class<?> clazz : beanClasses) {
            beanDependencyGraph.addElement(clazz, getClassDependencies(clazz));
        }
    }

    private void instantiateAndRegisterBeans(List<Class<?>> sortedBean) throws ReflectiveOperationException {
        for (Class<?> clazz : sortedBean) {
            Object beanInstance = instantiateBean(clazz);
            beanContainer.registerBean(clazz, beanInstance);
        }
    }

    private Object instantiateBean(Class<?> clazz) throws ReflectiveOperationException {
        Constructor<?> constructor = getConstructor(clazz);
        List<Object> dependencies = getClassDependencies(clazz)
                .stream()
                .map(this::resolveDependency)
                .toList();

        return constructor.newInstance(dependencies.toArray());
    }

    private List<Class<?>> getClassDependencies(Class<?> clazz) {
        Constructor<?> constructor = getConstructor(clazz);
        return List.of(constructor.getParameterTypes());
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            throw new NoPublicConstructorException(clazz);
        }
        return constructors[0];
    }

    private Object resolveDependency(Class<?> dependencyClass) {
        return beanContainer.getBean(dependencyClass);
    }

    static class NoPublicConstructorException extends RuntimeException {
        NoPublicConstructorException(Class<?> cls) {
            super("No public constructor found for class: " + cls.getName());
        }
    }
}