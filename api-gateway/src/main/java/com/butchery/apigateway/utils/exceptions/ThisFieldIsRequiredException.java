package com.butchery.apigateway.utils.exceptions;

public class ThisFieldIsRequiredException extends RuntimeException{

    public ThisFieldIsRequiredException() {}

    public ThisFieldIsRequiredException(String message) {
        super(message);
    }


    public ThisFieldIsRequiredException(Throwable cause) {
        super(cause);
    }

    public ThisFieldIsRequiredException(String message, Throwable cause) {
        super(message, cause);
    }


}
