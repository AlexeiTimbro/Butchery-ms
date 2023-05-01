package com.butchery.meatservice.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

//@EqualsAndHashCode(callSuper = false)
@Value
@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MeatRequestModel {


    private String animal;
    private String environment;
    private String texture;
    private String expirationDate;
    private Integer price;
}
