package com.butchery.purchaseservice.domainclientlayer.meat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeatRequestModel {

    private String animal;
    private Status status;
    private String environment;
    private String texture;
    private String expirationDate;
    private Double price;
}
