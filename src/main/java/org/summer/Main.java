package org.summer;
import org.summer.core.ApplicationContextFactory;

public class Main {
    public static void main(String[] args)  {
        ApplicationContextFactory
                .getDefaultApplicationContext()
                .initialize("org.summer");
    }
}