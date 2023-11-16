package org.summer.core.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DependencyResolver {

    /**
     * Gets the dependencies (constructor parameter types) for a given class.
     * @param clazz The class to analyze.
     * @return A list of classes representing the constructor parameters.
     */
    public List<Class<?>> getDependenciesForClass(Class<?> clazz) {
        Constructor<?> constructor = getConstructor(clazz);
        return Arrays.stream(constructor.getParameterTypes()).collect(Collectors.toList());
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new NoPublicConstructorException(clazz);
        }

        return constructors[0];
    }

    static class NoPublicConstructorException extends RuntimeException {
        NoPublicConstructorException(Class<?> cls) {
            super("No public constructor found for class: " + cls.getName());
        }
    }
}