package com.butchery.apigateway.presentationlayer;

import jdk.jshell.Snippet;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper=false)
@Data
@Builder
@AllArgsConstructor
public class MeatRequestModel extends RepresentationModel<MeatRequestModel> {

    String animal;
    Status status;
    String environment;
    String texture;
    String expirationDate;
    Integer price;

}
