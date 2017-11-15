package com.kaishengit.crm.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Administrator.
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbidException extends RuntimeException {

    public ForbidException() {}

    public ForbidException(String message) {
        super(message);
    }

}
