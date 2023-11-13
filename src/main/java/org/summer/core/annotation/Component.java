package org.summer.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Indicates that this annotation can be used on classes or interfaces
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}
