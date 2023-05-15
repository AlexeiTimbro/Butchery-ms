package com.butchery.purchaseservice.Utils.Exceptions;

public class PurchaseDateIsNotValid extends RuntimeException{

    public PurchaseDateIsNotValid () {}

    public PurchaseDateIsNotValid (String message) { super(message); }

    public PurchaseDateIsNotValid (Throwable cause) { super(cause); }

    public PurchaseDateIsNotValid (String message, Throwable cause) { super(message, cause); }
}
