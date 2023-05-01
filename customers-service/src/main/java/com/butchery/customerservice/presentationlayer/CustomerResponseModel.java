package com.butchery.customerservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
public class CustomerResponseModel extends RepresentationModel<CustomerResponseModel> {

    public CustomerResponseModel(){

    }
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String street;
    private String city;
    private String province;
    private String country;
    private String postalCode;

}
