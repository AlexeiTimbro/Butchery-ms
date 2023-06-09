package com.butchery.butcherservice.datalayer;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class ButcherIdentifier {

    private String butcherId;

    ButcherIdentifier() {
       this.butcherId = UUID.randomUUID().toString();
    }

    public ButcherIdentifier(String butcherId) {
        this.butcherId = butcherId;
    }

    public String getButcherId() {
        return butcherId;
    }
}
