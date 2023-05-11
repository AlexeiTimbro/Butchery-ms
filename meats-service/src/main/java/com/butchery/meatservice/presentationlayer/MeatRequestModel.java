package com.butchery.meatservice.presentationlayer;

import com.butchery.meatservice.datalayer.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class MeatRequestModel {

    private String animal;
    private Status status;
    private String environment;
    private String texture;
    private String expirationDate;
    private Double price;
}
