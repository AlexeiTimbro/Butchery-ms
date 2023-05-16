package com.butchery.butcherservice.presentationlayer;

import lombok.*;

//@EqualsAndHashCode(callSuper = false)
@Value
@Builder
@AllArgsConstructor
public class ButcherRequestModel {

    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String phoneNumber;
    private Double salary;
    private Double commissionRate;
    private String street;
    private String city;
    private String province;
    private String country;
    private String postalCode;

}
