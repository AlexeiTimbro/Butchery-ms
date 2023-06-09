package com.butchery.purchaseservice.domainclientlayer.meat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeatResponseModel {

    private String meatId;
    private String animal;
    private Status status;
    private String environment;
    private String texture;
    private String expirationDate;
    private Double price;
}
