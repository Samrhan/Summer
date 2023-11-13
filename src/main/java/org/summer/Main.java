package org.summer;

import org.summer.core.ApplicationContext;

public class Main {
    public static void main(String[] args) throws Exception {
        new ApplicationContext().initialize("org.summer");
    }
}