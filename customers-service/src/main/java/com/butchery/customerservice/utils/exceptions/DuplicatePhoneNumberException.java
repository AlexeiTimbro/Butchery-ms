package com.butchery.customerservice.utils.exceptions;

public class DuplicatePhoneNumberException extends RuntimeException{

    //public DuplicatePhoneNumberException(){

    //}
    public DuplicatePhoneNumberException(String message){
        super(message);
    }
    /*
    public DuplicatePhoneNumberException(Throwable cause){
        super(cause);
    }
    public DuplicatePhoneNumberException(String message, Throwable cause){
        super(message,cause);
    }

     */
}
