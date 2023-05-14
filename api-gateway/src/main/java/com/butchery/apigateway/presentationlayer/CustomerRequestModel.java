package com.butchery.apigateway.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

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
