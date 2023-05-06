package com.butchery.apigateway.presentationlayer;

import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class MeatRequestModel extends RepresentationModel<MeatRequestModel> {

    String animal;
    Status status;
    String environment;
    String texture;
    String expirationDate;
    Integer price;
}
