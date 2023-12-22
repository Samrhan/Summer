package org.summer.core.util.exception;

public class DependencyNotInGraphException extends RuntimeException {
    public DependencyNotInGraphException(String message) {
        super(message);
    }

}
