package com.butchery.apigateway.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MeatResponseModel extends RepresentationModel<MeatResponseModel> {

    String meatId;
    String animal;
    Status status;
    String environment;
    String texture;
    String expirationDate;
    Double price;
}
