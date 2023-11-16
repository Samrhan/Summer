package org.summer.core.bean;

import java.lang.reflect.Constructor;
import java.util.List;

public class BeanFactory {

    private final BeanContainer beanContainer;

    public BeanFactory(BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
    }

    public void instantiateAndRegisterBeans(List<Class<?>> sortedBeans) throws ReflectiveOperationException {
        for (Class<?> clazz : sortedBeans) {
            Object beanInstance = instantiateBean(clazz);
            beanContainer.registerBean(clazz, beanInstance);
        }
    }

    private Object instantiateBean(Class<?> clazz) throws ReflectiveOperationException {
        Constructor<?> constructor = getConstructor(clazz);
        List<Object> dependencies = getConstructorDependencies(constructor)
                .stream()
                .map(this::resolveDependency)
                .toList();

        return constructor.newInstance(dependencies.toArray());
    }

    private List<Class<?>> getConstructorDependencies(Constructor<?> constructor) {
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