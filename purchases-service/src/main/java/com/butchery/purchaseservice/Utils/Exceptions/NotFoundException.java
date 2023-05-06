package com.butchery.purchaseservice.Utils.Exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException() {}

    public NotFoundException(String message) { super(message); }

    public NotFoundException(Throwable cause) { super(cause); }

    public NotFoundException(String message, Throwable cause) { super(message, cause); }
}