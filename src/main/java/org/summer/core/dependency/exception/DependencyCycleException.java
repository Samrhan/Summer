package org.summer.core.dependency.exception;

public class DependencyCycleException extends RuntimeException {
    public DependencyCycleException(String message) {
        super(message);
    }
}