package org.summer.core.util;

import org.summer.core.util.exception.DependencyCycleException;
import org.summer.core.util.exception.DependencyNotInGraphException;

import java.util.*;

/**
 * Represents a dependency graph for beans.
 * This class provides functionality to add beans and their dependencies, and to perform
 * topological sorting to resolve the order in which beans should be instantiated.
 */
public class DependencyGraph<T> {
    private final Map<T, Set<T>> graph = new HashMap<>();

    public void addElement(T element, List<T> dependencies) {
        graph.put(element, new HashSet<>(dependencies));
    }

    public List<T> build() throws DependencyCycleException, DependencyNotInGraphException {
        return topologicalSort();
    }

    /**
     * Sorts objects in a way that respects their dependencies.
     *
     * @return A list of sorted object.
     * @throws DependencyCycleException if a dependency cycle is detected.
     */
    public List<T> topologicalSort() {
        Set<T> visited = new HashSet<>();
        Set<T> visiting = new HashSet<>();
        List<T> sortedBeans = new LinkedList<>();

        for (T bean : graph.keySet()) {
            if (!visited.contains(bean)) {
                visitBean(bean, visited, visiting, sortedBeans);
            }
        }
        return sortedBeans;
    }

    private void visitBean(T bean, Set<T> visited, Set<T> visiting, List<T> sortedBeans) {
        detectCycle(bean, visiting);

        if (visited.contains(bean)) {
            return;
        }

        visiting.add(bean);
        Set<T> dependencies = graph.get(bean);
        if (dependencies == null) {
            throw new DependencyNotInGraphException("%s isn't present in the graph".formatted(bean.toString()));
        }
        for (T dependency : dependencies) {
            visitBean(dependency, visited, visiting, sortedBeans);
        }
        visiting.remove(bean);
        visited.add(bean);

        sortedBeans.add(bean);
    }

    private void detectCycle(T bean, Set<T> visiting) {
        if (visiting.contains(bean)) {
            throw new DependencyCycleException("Cycle detected in dependency graph for : %s".formatted(bean));
        }
    }
}
