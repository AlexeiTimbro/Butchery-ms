package com.butchery.apigateway.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class ButcherResponseModel extends RepresentationModel<ButcherResponseModel> {

    String butcherId;
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
