package com.butchery.purchaseservice.datalayer;

import java.util.UUID;

public class CustomerIdentifier {

    private String customerId;

    public CustomerIdentifier(String customerId){
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
