package com.butchery.purchaseservice.domainclientlayer.meat;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class MeatResponseModel extends RepresentationModel<MeatResponseModel> {

    final String meatId;
    final String animal;
    final Status status;
    final String environment;
    final String texture;
    final String expirationDate;
    final Integer price;
}
