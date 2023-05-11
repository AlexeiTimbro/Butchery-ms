package com.butchery.purchaseservice.domainclientlayer.butcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ButcherResponseModel {



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
