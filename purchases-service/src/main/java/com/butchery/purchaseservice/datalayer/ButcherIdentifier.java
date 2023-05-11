package com.butchery.purchaseservice.datalayer;

import java.util.UUID;

public class ButcherIdentifier {

    private String butcherId;

    public ButcherIdentifier(String butcherId) {
        this.butcherId = butcherId;
    }

    public String getButcherId() {
        return butcherId;
    }
}
