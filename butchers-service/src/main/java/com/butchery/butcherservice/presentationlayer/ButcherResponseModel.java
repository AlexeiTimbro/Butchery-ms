package com.butchery.butcherservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

//@Value
//@Builder
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
public class ButcherResponseModel extends RepresentationModel<ButcherResponseModel> {

    public ButcherResponseModel(){

    }
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
