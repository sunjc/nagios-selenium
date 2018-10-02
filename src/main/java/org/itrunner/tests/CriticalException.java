package org.itrunner.tests;

public class CriticalException extends RuntimeException {


    public CriticalException(String message) {
        super(message);
    }

    public CriticalException(String message, Exception cause) {
        super(message, cause);
    }

}
