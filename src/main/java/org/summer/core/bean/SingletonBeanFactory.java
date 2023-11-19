package org.summer.core.bean;

import org.summer.core.DependencyResolver;
import org.summer.core.BeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.summer.core.util.DependencyReflectionUtil.getConstructor;
import static org.summer.core.util.DependencyReflectionUtil.getConstructorDependencies;

public class SingletonBeanFactory implements BeanFactory {

    private final DependencyResolver dependencyResolver;

    public SingletonBeanFactory(DependencyResolver dependencyResolver) {
        this.dependencyResolver = dependencyResolver;
    }

    @Override
    public <T> T createBean(Class<T> clazz) {
        Constructor<T> constructor = getConstructor(clazz);
        List<?> dependencies = getConstructorDependencies(constructor)
                .stream()
                .map(dependencyResolver::resolveDependency)
                .toList();

        try {
            return constructor.newInstance(dependencies.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}