package org.summer.core;

import org.summer.core.annotation.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class PackageScanner {

    public List<Class<?>> scanPackageForBeans(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = convertPackageNameToPath(packageName);
        List<File> dirs = getDirectoriesFromResources(classLoader, path);

        return dirs.stream()
                .flatMap(directory -> findClasses(directory, packageName).stream())
                .collect(Collectors.toList());
    }

    private String convertPackageNameToPath(String packageName) {
        // Convert package name to a directory path
        return packageName.replace('.', '/');
    }

    private List<File> getDirectoriesFromResources(ClassLoader classLoader, String path) throws IOException {
        // Get directories from class loader resources
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        return dirs;
    }

    public List<Class<?>> findClasses(File directory, String packageName)  {
        if (!directory.exists()) {
            return Collections.emptyList();
        }

        return Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .flatMap(file -> processFile(file, packageName).stream())
                .collect(Collectors.toList());
    }

    private List<Class<?>> processFile(File file, String packageName) {
        try {
            if (file.isDirectory()) {
                return findClasses(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                Class<?> cls = loadClass(file, packageName);
                if (cls.isAnnotationPresent(Component.class)) {
                    return Collections.singletonList(cls);
                }
            }
        } catch (ClassNotFoundException e) {
            // Handle the exception or propagate it
        }
        return Collections.emptyList();
    }

    private Class<?> loadClass(File file, String packageName) throws ClassNotFoundException {
        String className = packageName + '.' + getClassName(file);
        return Class.forName(className);
    }

    private String getClassName(File file) {
        return file.getName().substring(0, file.getName().length() - 6);
    }
}