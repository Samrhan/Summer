package org.summer.core.bean;

import org.summer.core.DependencyGraph;
import org.summer.core.bean.exception.DependencyCycleException;
import org.summer.core.bean.exception.DependencyNotInGraphException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a dependency graph for beans.
 * This class provides functionality to add beans and their dependencies, and to perform
 * topological sorting to resolve the order in which beans should be instantiated.
 */
public class BeanDependencyGraph implements DependencyGraph<Class<?>> {
    private final Map<Class<?>, Set<Class<?>>> graph = new HashMap<>();

    @Override
    public void addElement(Class<?> cls, List<Class<?>> dependencies) {
        addBeanClass(cls, dependencies);
    }

    void addBeanClass(Class<?> cls, List<Class<?>> dependencies) {
        graph.put(cls, new HashSet<>(dependencies));
    }

    @Override
    public List<Class<?>> build() throws DependencyCycleException, DependencyNotInGraphException {
         return new ArrayList<>(topologicalSort());
    }

    /**
     * Sorts the beans in a way that respects their dependencies.
     *
     * @return A list of sorted bean names.
     * @throws DependencyCycleException if a dependency cycle is detected.
     */
    List<Class<?>> topologicalSort() {
        Set<Class<?>> visited = new HashSet<>();
        Set<Class<?>> visiting = new HashSet<>();
        List<Class<?>> sortedBeans = new LinkedList<>();

        for (Class<?> bean : graph.keySet()) {
            if (!visited.contains(bean)) {
                visitBean(bean, visited, visiting, sortedBeans);
            }
        }
        return sortedBeans;
    }

    private void visitBean(Class<?> bean, Set<Class<?>> visited,  Set<Class<?>> visiting, List<Class<?>> sortedBeans) {
        detectCycle(bean, visiting);

        if (visited.contains(bean)) {
            return;
        }

        visiting.add(bean);
        Set<Class<?>> dependencies = graph.get(bean);
        if(dependencies == null){
            throw new DependencyNotInGraphException("Class %s isn't present in the graph".formatted(bean.getName()));
        }
        for (Class<?> dependency : dependencies) {
            visitBean(dependency, visited, visiting, sortedBeans);
        }
        visiting.remove(bean);
        visited.add(bean);

        sortedBeans.add(bean);
    }

    private void detectCycle(Class<?> bean, Set<Class<?>> visiting){
        if (visiting.contains(bean)) {
            throw new DependencyCycleException("Cycle detected in dependency graph for bean: %s".formatted(bean));
        }
    }
}
