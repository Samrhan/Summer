package org.summer.core.dependency;

import org.summer.core.dependency.exception.DependencyCycleException;

import java.util.List;

public interface DependencyGraph<T> {
    /**
     * Adds an element and its dependencies to the dependency graph.
     * @param element The element to be added.
     */
    void addElement(T element, List<T> dependencies);

    /**
     * Sorts or resolves the dependencies in the graph.
     * @return A list representing the sorted order of the elements.
     * @throws DependencyCycleException If a cycle is detected in the dependencies.
     */
    List<T> build() throws DependencyCycleException;

    /**
     * Retrieves an element from the graph.
     * @param identifier An identifier for the element.
     * @return The retrieved element.
     */
    T getElement(String identifier);
}