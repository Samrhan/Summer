package org.summer.core.context;

import org.summer.core.ApplicationContext;
import org.summer.core.BeanFactory;
import org.summer.core.BeanStore;
import org.summer.core.DependencyManager;
import org.summer.core.annotation.Component;
import org.summer.core.util.PackageScanner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingletonBasedApplicationContext implements ApplicationContext {

    private final BeanStore beanStore;
    private final BeanFactory beanFactory;
    private final PackageScanner packageScanner;
    private final DependencyManager dependencyManager;

    public SingletonBasedApplicationContext(BeanStore beanStore, BeanFactory beanFactory, PackageScanner packageScanner, DependencyManager dependencyManager) {
        this.beanStore = beanStore;
        this.beanFactory = beanFactory;
        this.packageScanner = packageScanner;
        this.dependencyManager = dependencyManager;
    }

    @Override
    public void initialize(String basePackage) {
        List<Class<?>> beans = scanBeans(basePackage);
        registerBeans(beans);
    }

    private void registerBeans(List<Class<?>> sortedBeans) {
        sortedBeans.forEach(this::createBean);
    }

    private <T> void createBean(Class<T> clazz) {
        if (!beanStore.contains(clazz)) {
            T bean = beanFactory.createBean(clazz);
            beanStore.registerBean(clazz, bean);
        }
    }

    private List<Class<?>> scanBeans(String basePackage) {
        Set<Class<?>> classes = scanForBeanClasses(basePackage);
        return dependencyManager.sort(classes);
    }

    public Set<Class<?>> scanForBeanClasses(String basePackage) {
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
}