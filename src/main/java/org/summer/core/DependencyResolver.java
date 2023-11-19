package org.summer.core;

public interface DependencyResolver {
    <T> T resolveDependency(Class<T> clazz);
}
