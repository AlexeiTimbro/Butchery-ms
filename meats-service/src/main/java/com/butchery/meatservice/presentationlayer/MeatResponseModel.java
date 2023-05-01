package com.butchery.meatservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
public class MeatResponseModel extends RepresentationModel<MeatResponseModel> {

    private String meatId;
    private String animal;
    private String environment;
    private String texture;
    private String expirationDate;
    private Integer price;
}
