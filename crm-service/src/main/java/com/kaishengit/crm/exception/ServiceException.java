package com.kaishengit.crm.exception;

/**
 * @author Administrator.
 */
public class ServiceException extends RuntimeException {

    public ServiceException() {}

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException (Throwable th) {
        super(th);
    }

    public ServiceException (String message, Throwable th) {
        super(message,th);
    }

}
