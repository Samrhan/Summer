package org.summer.core;

import org.summer.core.bean.BeanDependencyManager;
import org.summer.core.bean.SingletonBeanContainer;
import org.summer.core.bean.SingletonBeanFactory;
import org.summer.core.context.SingletonBasedApplicationContext;
import org.summer.core.util.DependencyGraph;
import org.summer.core.util.PackageScanner;
import org.summer.core.util.ResourcesScanner;

public class ApplicationContextFactory {
    public static ApplicationContext createSingletonApplicationContext() {
        BeanStore singletonBeanStore = new SingletonBeanContainer();
        BeanFactory singletonBeanFactory = new SingletonBeanFactory(singletonBeanStore);
        PackageScanner packageScanner = new ResourcesScanner();
        DependencyGraph<Class<?>> beanDependencyGraph = new DependencyGraph<>();
        DependencyManager dependencyManager = new BeanDependencyManager(beanDependencyGraph);

        return new SingletonBasedApplicationContext(singletonBeanStore, singletonBeanFactory, packageScanner, dependencyManager);
    }

    public static ApplicationContext getDefaultApplicationContext() {
        return createSingletonApplicationContext();
    }
}
