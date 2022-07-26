package org.winry.exception;

public class NotLoginException extends RuntimeException {

    public NotLoginException() {
        super();
    }

    public NotLoginException(String s) {
        super(s);
    }

    public NotLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotLoginException(Throwable cause) {
        super(cause);
    }
}
