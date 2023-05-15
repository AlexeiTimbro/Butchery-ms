package com.butchery.apigateway.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper=false)
@Data
@Builder
@AllArgsConstructor
public class CustomerRequestModel extends RepresentationModel<CustomerRequestModel> {

    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String street;
    String city;
    String province;
    String country;
    String postalCode;

}
