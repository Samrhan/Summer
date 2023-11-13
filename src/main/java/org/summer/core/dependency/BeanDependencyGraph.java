package org.summer.core.dependency;

import org.summer.core.dependency.exception.DependencyCycleException;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a dependency graph for beans, typically used in dependency injection frameworks.
 * This class provides functionality to add beans and their dependencies, and to perform
 * topological sorting to resolve the order in which beans should be instantiated.
 */
public class BeanDependencyGraph implements DependencyGraph<Class<?>> {
    private final Map<String, Set<String>> graph = new HashMap<>();
    private final Map<String, Class<?>> beanClassMap = new HashMap<>();
    private final Set<String> visiting = new HashSet<>();

    /**
     * Adds a bean class to the graph, analyzing its constructor to determine dependencies.
     * @param cls The class of the bean to be added.
     */
    @Override
    public void addElement(Class<?> cls) {
        addBeanClass(cls); // Assuming addBeanClass is already defined
    }

    void addBeanClass(Class<?> cls) {
        Constructor<?>[] constructors = cls.getConstructors();
        if (constructors.length == 0) {
            throw new NoPublicConstructorException(cls);
        }
        Constructor<?> constructor = constructors[0];
        graph.put(cls.getName(), Arrays.stream(constructor.getParameterTypes())
                .map(Class::getName)
                .collect(Collectors.toSet()));
        beanClassMap.put(cls.getName(), cls);
    }

    @Override
    public List<Class<?>> sort() throws DependencyCycleException {
        // Convert the sorted bean names to Class objects
        return sortBean().stream()
                .map(this::getElement)
                .collect(Collectors.toList());
    }
    /**
     * Sorts the beans in a way that respects their dependencies.
     * @return A list of sorted bean names.
     * @throws DependencyCycleException if a dependency cycle is detected.
     */
    List<String> sortBean() {
        Set<String> visited = new HashSet<>();
        List<String> sortedBeans = new ArrayList<>();
        for (String bean : graph.keySet()) {
            if (!visited.contains(bean)) {
                visitBean(bean, visited, sortedBeans);
            }
        }
        return sortedBeans;
    }

    private void visitBean(String bean, Set<String> visited, List<String> sortedBeans) {
        if (visiting.contains(bean)) {
            throw new DependencyCycleException("Cycle detected in dependency graph for bean: " + bean);
        }
        if (!visited.contains(bean)) {
            visiting.add(bean);  // Mark bean as visiting
            performTopologicalSort(bean, visited, sortedBeans);
            finalizeVisit(bean, visited, sortedBeans);  // Mark bean as visited
        }
    }

    private void performTopologicalSort(String bean, Set<String> visited, List<String> sortedBeans) {
        graph.getOrDefault(bean, Collections.emptySet())
                .forEach(dependency -> visitBean(dependency, visited, sortedBeans));
    }

    private void finalizeVisit(String bean, Set<String> visited, List<String> sortedBeans) {
        visiting.remove(bean);  // Remove bean from visiting
        visited.add(bean);  // Mark bean as visited
        sortedBeans.add(bean);  // Add bean to the sorted list
    }

    @Override
    public Class<?> getElement(String beanName) {
        return beanClassMap.get(beanName);
    }

    static class NoPublicConstructorException extends RuntimeException {
        NoPublicConstructorException(Class<?> cls) {
            super("No public constructor found for class: " + cls.getName());
        }
    }
}
