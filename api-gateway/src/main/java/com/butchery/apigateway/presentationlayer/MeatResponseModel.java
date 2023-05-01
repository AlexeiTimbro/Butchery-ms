package com.butchery.apigateway.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class MeatResponseModel extends RepresentationModel<MeatResponseModel> {

    String meatId;
    String animal;
    String environment;
    String texture;
    String expirationDate;
    Integer price;
}
