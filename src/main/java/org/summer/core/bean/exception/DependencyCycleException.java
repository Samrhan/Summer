package org.summer.core.bean.exception;

public class DependencyCycleException extends RuntimeException {
    public DependencyCycleException(String message) {
        super(message);
    }
}