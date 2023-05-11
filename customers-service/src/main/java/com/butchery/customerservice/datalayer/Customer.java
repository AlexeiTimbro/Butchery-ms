package com.butchery.customerservice.datalayer;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private CustomerIdentifier customerIdentifier;

    private String firstName;
    private String lastName;
    private String email;
    //@Column(unique=true)
    private String phoneNumber;
    private String street;
    private String city;
    private String province;
    private String country;
    private String postalCode;


    Customer(){
        this.customerIdentifier = new CustomerIdentifier();
    }


    public Customer(String firstName, String lastName, String email,String phoneNumber, String street, String city, String province, String country, String postalCode) {
        this.customerIdentifier = new CustomerIdentifier();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
    }
}
