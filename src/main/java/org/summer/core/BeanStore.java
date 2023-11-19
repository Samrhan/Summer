package org.summer.core;

public interface BeanStore extends DependencyResolver {
    <T> void registerBean(Class<T> clazz, T bean);

    <T> T getBean(Class<T> clazz);

    <T> boolean contains(Class<T> clazz);
}
