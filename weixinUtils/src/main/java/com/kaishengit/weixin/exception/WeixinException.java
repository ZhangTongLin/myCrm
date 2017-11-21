package com.kaishengit.weixin.exception;

/**
 * @author Administrator.
 */
public class WeixinException extends RuntimeException {

    public WeixinException() {}

    public WeixinException(String message) {
        super(message);
    }

    public WeixinException(String message,Throwable th){
        super(message,th);
    }

    public WeixinException(Throwable th) {
        super(th);
    }
}
