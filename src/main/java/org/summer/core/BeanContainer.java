package org.summer.core;

import java.util.HashMap;
import java.util.Map;

public class BeanContainer {

    private final Map<Class<?>, Object> beans = new HashMap<>();
    // Registers a singleton bean
    public void registerBean(Class<?> beanClass, Object beanInstance) {
        beans.put(beanClass, beanInstance);
    }

    // Retrieves a bean of the specified type
    public <T> T getBean(Class<T> beanClass) {
        return (T) beans.get(beanClass);
    }
}
