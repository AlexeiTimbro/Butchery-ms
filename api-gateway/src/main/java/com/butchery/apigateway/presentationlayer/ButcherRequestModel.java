package com.butchery.apigateway.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

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
