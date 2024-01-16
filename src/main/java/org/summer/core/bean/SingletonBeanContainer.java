package org.summer.core.bean;

import org.summer.core.BeanStore;

import java.util.HashMap;
import java.util.Map;

public class SingletonBeanContainer implements BeanStore {

    private final Map<Class<?>, Object> beans;

    {
        beans = new HashMap<>();
        beans.put(BeanStore.class, this);
    }

    // Registers a singleton bean
    public <T> void registerBean(Class<T> beanClass, T beanInstance) {
        beans.put(beanClass, beanInstance);
    }

    /**
     * Returns a singleton bean
     * Unchecked cast is safe because we only put instances of T into the map
     *
     * @param beanClass class of type T
     * @param <T>       type of bean
     * @return beanInstance of type T
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> beanClass) {
        return (T) beans.get(beanClass);
    }

    @Override
    public <T> boolean contains(Class<T> clazz) {
        return beans.containsKey(clazz);
    }

    @Override
    public <T> T resolveDependency(Class<T> clazz) {
        return this.getBean(clazz);
    }
}
