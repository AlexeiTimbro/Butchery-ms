package com.butchery.meatservice.presentationlayer;

import com.butchery.meatservice.datalayer.Status;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class MeatResponseModel extends RepresentationModel<MeatResponseModel> {

    private String meatId;
    private String animal;
    private Status status;
    private String environment;
    private String texture;
    private String expirationDate;
    private Double price;
}
