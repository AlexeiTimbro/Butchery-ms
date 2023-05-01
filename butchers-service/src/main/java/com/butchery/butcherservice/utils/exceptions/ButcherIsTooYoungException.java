package com.butchery.butcherservice.utils.exceptions;

public class ButcherIsTooYoungException extends RuntimeException{

    //public ButcherIsTooYoungException(){

    //}
    public ButcherIsTooYoungException(String message){
        super(message);
    }
    /*
    public ButcherIsTooYoungException(Throwable cause){
        super(cause);
    }
    public ButcherIsTooYoungException(String message, Throwable cause){
        super(message, cause);
    }

     */
}
