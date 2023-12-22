package org.summer.core.util.exception;

public class DependencyCycleException extends RuntimeException {
    public DependencyCycleException(String message) {
        super(message);
    }
}