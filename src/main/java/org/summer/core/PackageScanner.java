package org.summer.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PackageScanner {


    public Stream<Class<?>> scanPackages(String packageName) throws IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = convertPackageNameToPath(packageName);
        Set<File> dirs = getDirectoriesFromResources(classLoader, path);

        return dirs.parallelStream()
                .flatMap(directory -> findClasses(directory, packageName));

    }

    private String convertPackageNameToPath(String packageName) {
        // Convert package name to a directory path
        return packageName.replace('.', '/');
    }

    private Set<File> getDirectoriesFromResources(ClassLoader classLoader, String path) throws IOException {
        // Get directories from class loader resources
        Enumeration<URL> resources = classLoader.getResources(path);
        Set<File> dirs = new HashSet<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        return dirs;
    }

    public Stream<Class<?>> findClasses(File directory, String packageName) {
        if (!directory.exists()) {
            return Stream.empty();
        }

        return Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .flatMap(file -> processFile(file, packageName));
    }

    private Stream<Class<?>> processFile(File file, String packageName) {
        Stream<Class<?>> classStream = Stream.empty();

        if (file.isDirectory()) {
            return findClasses(file, packageName + "." + file.getName());
        } else if (file.getName().endsWith(".class")) {
            Class<?> cls = loadClass(file, packageName);
            classStream = Stream.of(cls);
        }

        return classStream;
    }

    private Class<?> loadClass(File file, String packageName) {
        String className = packageName + '.' + getClassName(file);
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new Error("Couldn't load " + file.getName());
        }
    }

    private String getClassName(File file) {
        return file.getName().substring(0, file.getName().length() - 6);
    }
}