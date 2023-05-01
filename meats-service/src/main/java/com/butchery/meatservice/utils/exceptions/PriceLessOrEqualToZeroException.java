package com.butchery.meatservice.utils.exceptions;

public class PriceLessOrEqualToZeroException extends RuntimeException{

    //public PriceLessOrEqualToZeroException() {}

    public PriceLessOrEqualToZeroException(String message) {
        super(message);
    }

    /*
    public PriceLessOrEqualToZeroException(Throwable cause) {
        super(cause);
    }

    public PriceLessOrEqualToZeroException(String message, Throwable cause) {
        super(message, cause);
    }

     */
}
