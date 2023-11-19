package org.summer.core;

import java.util.List;
import java.util.Set;

public interface DependencyManager {
    List<Class<?>> sort(Set<Class<?>> classes);
}