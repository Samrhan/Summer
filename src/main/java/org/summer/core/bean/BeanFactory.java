package org.summer.core.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;

import static org.summer.core.util.DependencyResolver.getConstructor;
import static org.summer.core.util.DependencyResolver.getConstructorDependencies;

public class BeanFactory {

    private final Function<Class<?>, Object> dependencyResolver;

    public BeanFactory(Function<Class<?>, Object> dependencyResolver) {
        this.dependencyResolver = dependencyResolver;
    }

    public Object createBean(Class<?> clazz) {
        Constructor<?> constructor = getConstructor(clazz);
        List<Object> dependencies = getConstructorDependencies(constructor)
                .stream()
                .map(dependencyResolver)
                .toList();

        try {
            return constructor.newInstance(dependencies.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}