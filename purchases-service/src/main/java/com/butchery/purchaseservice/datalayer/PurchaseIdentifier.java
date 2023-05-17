package com.butchery.purchaseservice.datalayer;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

public class PurchaseIdentifier {

    @Indexed(unique = true)
    private String purchaseId;

    public PurchaseIdentifier() {
        this.purchaseId = UUID.randomUUID().toString();
    }

    public PurchaseIdentifier(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }
}
