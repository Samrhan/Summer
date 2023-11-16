package org.summer.core.dependency;

import org.summer.core.dependency.exception.DependencyCycleException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a dependency graph for beans.
 * This class provides functionality to add beans and their dependencies, and to perform
 * topological sorting to resolve the order in which beans should be instantiated.
 */
public class BeanDependencyGraph implements DependencyGraph<Class<?>> {
    private final Map<String, Set<String>> graph = new HashMap<>();
    private final Map<String, Class<?>> beanClassMap = new HashMap<>();

    /**
     * Adds a bean class to the graph, analyzing its constructor to determine dependencies.
     *
     * @param cls The class of the bean to be added.
     */
    @Override
    public void addElement(Class<?> cls, List<Class<?>> dependencies) {
        addBeanClass(cls, dependencies); // Assuming addBeanClass is already defined
    }

    void addBeanClass(Class<?> cls, List<Class<?>> dependencies) {
        graph.put(cls.getName(), dependencies.stream()
                .map(Class::getName)
                .collect(Collectors.toSet()));
        beanClassMap.put(cls.getName(), cls);
    }

    @Override
    public List<Class<?>> build() throws DependencyCycleException {
        // Convert the sorted bean names to Class objects
         return topologicalSort()
                .stream()
                .map(this::getElement)
                .collect(Collectors.toList());
    }

    /**
     * Sorts the beans in a way that respects their dependencies.
     *
     * @return A list of sorted bean names.
     * @throws DependencyCycleException if a dependency cycle is detected.
     */
    List<String> topologicalSort() {
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();
        List<String> sortedBeans = new LinkedList<>();

        for (String bean : graph.keySet()) {
            if (!visited.contains(bean)) {
                visitBean(bean, visited, visiting, sortedBeans);
            }
        }
        return sortedBeans;
    }

    private void visitBean(String bean, Set<String> visited,  Set<String> visiting, List<String> sortedBeans) {
        detectCycle(bean, visiting);

        if (visited.contains(bean)) {
            return;
        }

        visiting.add(bean);
        for (String dependency : graph.getOrDefault(bean, Collections.emptySet())) {
            visitBean(dependency, visited, visiting, sortedBeans);
        }
        visiting.remove(bean);
        visited.add(bean);

        sortedBeans.add(bean);
    }

    private void detectCycle(String bean, Set<String> visiting){
        if (visiting.contains(bean)) {
            throw new DependencyCycleException("Cycle detected in dependency graph for bean: " + bean);
        }
    }


    @Override
    public Class<?> getElement(String beanName) {
        return beanClassMap.get(beanName);
    }
}
