package com.butchery.apigateway.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class MeatRequestModel extends RepresentationModel<MeatRequestModel> {

    String animal;
    String environment;
    String texture;
    String expirationDate;
    Integer price;
}
