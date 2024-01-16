package org.summer.core.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DependencyReflectionUtil {

    /**
     * Gets the dependencies (constructor parameter types) for a given class.
     *
     * @param clazz The class to analyze.
     * @return A list of classes representing the constructor parameters.
     */
    public static <T> List<Class<?>> getDependenciesForClass(Class<T> clazz) {
        Constructor<T> constructor = getConstructor(clazz);
        return Arrays.stream(constructor.getParameterTypes()).collect(Collectors.toList());
    }

    public static <T> List<Class<?>> getConstructorDependencies(Constructor<T> constructor) {
        return List.of(constructor.getParameterTypes());
    }

    /**
     * Gets the constructor with the most parameters for a given class.
     * Unchecked cast is safe because the constructor is guaranteed to be of type T.
     *
     * @param clazz The class to analyze.
     * @return The constructor with the most parameters.
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructor(Class<T> clazz) {
        Constructor<T>[] constructors = (Constructor<T>[]) clazz.getConstructors();

        if (constructors.length == 0) {
            throw new NoPublicConstructorException(clazz);
        }

        return Arrays.stream(constructors)
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .get();

    }

    static class NoPublicConstructorException extends RuntimeException {
        NoPublicConstructorException(Class<?> clazz) {
            super("No public constructor found for class: " + clazz.getName());
        }
    }
}