package org.summer.core.bean;

import org.summer.core.DependencyManager;
import org.summer.core.util.DependencyGraph;
import org.summer.core.util.DependencyReflectionUtil;

import java.util.List;
import java.util.Set;

public class BeanDependencyManager implements DependencyManager {
    private final DependencyGraph<Class<?>> beanDependencyGraph;

    public BeanDependencyManager(DependencyGraph<Class<?>> beanDependencyGraph) {
        this.beanDependencyGraph = beanDependencyGraph;
    }

    @Override
    public List<Class<?>> sort(Set<Class<?>> classes){

        for (Class<?> clazz : classes) {
            List<Class<?>> dependencies = DependencyReflectionUtil.getDependenciesForClass(clazz);
            beanDependencyGraph.addElement(clazz, dependencies);
        }

        return beanDependencyGraph.build();
    }
}
