package com.butchery.apigateway.utils.exceptions;

public class InvalidEmailAddressException extends RuntimeException{

    public InvalidEmailAddressException(){

    }
    public InvalidEmailAddressException(String message){
        super(message);
    }

    public InvalidEmailAddressException(Throwable cause){
        super(cause);
    }
    public InvalidEmailAddressException(String message, Throwable cause){
        super(message,cause);
    }


}
