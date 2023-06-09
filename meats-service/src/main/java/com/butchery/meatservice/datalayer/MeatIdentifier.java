package com.butchery.meatservice.datalayer;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class MeatIdentifier {

    private String meatId;

    public MeatIdentifier(){
        this.meatId = UUID.randomUUID().toString();
    }

    public MeatIdentifier(String meatId) {
        this.meatId = meatId;
    }

    public String getMeatId() {
        return meatId;
    }
}
