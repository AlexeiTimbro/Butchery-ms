package com.butchery.customerservice.datalayer;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class CustomerIdentifier {

    private String customerId;

    CustomerIdentifier(){
        this.customerId = UUID.randomUUID().toString();
    }

    /*
    public CustomerIdentifier(String customerId){
        this.customerId = customerId;
    }

    */

    public String getCustomerId() {
        return customerId;
    }
}
