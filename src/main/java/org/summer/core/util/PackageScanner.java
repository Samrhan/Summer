package org.summer.core.util;

import java.io.File;
import java.util.stream.Stream;

public interface PackageScanner {
    Stream<Class<?>> scanPackages(String packageName);
}
