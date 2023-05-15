package com.butchery.apigateway.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper=false)
@Data
@Builder
@AllArgsConstructor
public class ButcherRequestModel extends RepresentationModel<ButcherRequestModel> {

    String firstName;
    String lastName;
    Integer age;
    String email;
    String phoneNumber;
    Double salary;
    Double commissionRate;
    String street;
    String city;
    String province;
    String country;
    String postalCode;

}
