package org.summer.core.exception;

public class DependencyCycleException extends RuntimeException {
    public DependencyCycleException(String message) {
        super(message);
    }

    // You can also add constructors for different use cases,
    // for example, one that also includes the cause of the exception
    public DependencyCycleException(String message, Throwable cause) {
        super(message, cause);
    }
}