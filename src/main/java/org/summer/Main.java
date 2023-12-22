package org.summer;

import org.summer.core.ApplicationContextFactory;

public class Main {
    public static void main(String[] args) {
        String packageName = args.length > 0 ? args[0] : "org.summer.example";
        ApplicationContextFactory
                .getDefaultApplicationContext()
                .initialize(packageName);
    }
}