package org.summer.core.dependency;

import org.summer.core.annotation.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PackageScanner {


    public List<Class<?>> scanPackageForBeans(String packageName) throws IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = convertPackageNameToPath(packageName);
        List<File> dirs = getDirectoriesFromResources(classLoader, path);

        return dirs.parallelStream()
                .flatMap(directory -> findClasses(directory, packageName))
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

    public Stream<Class<?>> findClasses(File directory, String packageName)  {
        if (!directory.exists()) {
            return Stream.empty();
        }

        return Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .flatMap(file -> processFile(file, packageName));
    }

    private Stream<Class<?>> processFile(File file, String packageName) {
        Stream<Class<?>> classStream = Stream.empty();

        try {
            if (file.isDirectory()) {
                return findClasses(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class") && isComponentClass(file, packageName)) {
                Class<?> cls = loadClass(file, packageName);
                classStream = Stream.of(cls);
            }
        } catch (ClassNotFoundException e) {
            // Handle the exception or propagate it
        }
        return classStream;
    }

    private boolean isComponentClass(File file, String packageName) throws ClassNotFoundException {
        // Perform efficient checks on the file or its bytecode, possibly using ASM or similar
        // For now, it performs a simple load-and-check
        Class<?> cls = loadClass(file, packageName);
        return cls.isAnnotationPresent(Component.class);
    }

    private Class<?> loadClass(File file, String packageName) throws ClassNotFoundException {
        String className = packageName + '.' + getClassName(file);
        return Class.forName(className);
    }

    private String getClassName(File file) {
        return file.getName().substring(0, file.getName().length() - 6);
    }
}