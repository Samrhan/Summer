package org.summer.core.bean.exception;

public class DependencyNotInGraphException extends RuntimeException {
    public DependencyNotInGraphException(String message) {
        super(message);
    }

}
