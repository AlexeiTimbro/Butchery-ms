package com.butchery.purchaseservice.datalayer;

import java.util.UUID;

public class MeatIdentifier {

    private String meatId;

    public MeatIdentifier(String meatId) {
        this.meatId = meatId;
    }

    public String getMeatId() {
        return meatId;
    }
}
