package org.summer.core.util;

import org.summer.core.bean.exception.DependencyCycleException;
import org.summer.core.bean.exception.DependencyNotInGraphException;

import java.util.List;

public interface DependencyGraph<T> {
    /**
     * Adds an element and its dependencies to the dependency graph.
     *
     * @param element The element to be added.
     */
    void addElement(T element, List<T> dependencies);

    /**
     * Sorts or resolves the dependencies in the graph.
     *
     * @return A list representing the sorted order of the elements.
     * @throws DependencyCycleException      If a cycle is detected in the dependencies.
     * @throws DependencyNotInGraphException If a dependency is not in the graph.
     */
    List<T> build() throws DependencyCycleException, DependencyNotInGraphException;
}