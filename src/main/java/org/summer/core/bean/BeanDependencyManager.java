package org.summer.core.bean;

import org.summer.core.DependencyManager;
import org.summer.core.util.DependencyReflectionUtil;

import java.util.List;
import java.util.Set;

public class BeanDependencyManager implements DependencyManager {

    @Override
    public List<Class<?>> sort(Set<Class<?>> classes) {
        BeanDependencyGraph beanDependencyGraph = new BeanDependencyGraph();

        for (Class<?> clazz : classes) {
            List<Class<?>> dependencies = DependencyReflectionUtil.getDependenciesForClass(clazz);
            beanDependencyGraph.addElement(clazz, dependencies);
        }

        return beanDependencyGraph.build();
    }
}
