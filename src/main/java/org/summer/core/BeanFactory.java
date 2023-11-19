package org.summer.core;

public interface BeanFactory {
    <T> T createBean(Class<T> clazz);
}
