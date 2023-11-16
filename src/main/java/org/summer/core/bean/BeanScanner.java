package org.summer.core.bean;

import org.summer.core.util.PackageScanner;
import org.summer.core.annotation.Component;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanScanner {

    private final PackageScanner packageScanner;

    public BeanScanner() {
        this.packageScanner = new PackageScanner();
    }

    public Set<Class<?>> scanForBeanClasses(String basePackage) throws IOException {
        Stream<Class<?>> classes = packageScanner.scanPackages(basePackage);
        return filterComponentClasses(classes);
    }

    private Set<Class<?>> filterComponentClasses(Stream<Class<?>> classes) {
        return classes
                .filter(this::isComponentClass)
                .collect(Collectors.toSet());
    }

    private boolean isComponentClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }
}