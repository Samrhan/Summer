package org.summer.core;

public interface BeanStoreFactory {
    BeanStore getBeanStore(Class<?> clazz);

}
