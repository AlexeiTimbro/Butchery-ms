package com.butchery.customerservice.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

//@EqualsAndHashCode(callSuper = false)
@Value
@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CustomerRequestModel {

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
