package com.butchery.apigateway.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MeatResponseModel extends RepresentationModel<MeatResponseModel> {

    String meatId;
    Status status;
    String animal;
    String environment;
    String texture;
    String expirationDate;
    Integer price;
}
