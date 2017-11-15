package com.kaishengit.crm.exception;

/**
 * @author Administrator.
 */
public class VerifyException extends RuntimeException {

    public VerifyException() {}

    public VerifyException(String message) {
        super(message);
    }

    public VerifyException(Throwable th) {
        super(th);
    }

    public VerifyException(String message, Throwable th) {
        super(message,th);
    }

}
