package com.butchery.butcherservice.datalayer;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="butchers")
@Data
public class Butcher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //private identifier

    @Embedded
    private ButcherIdentifier butcherIdentifier;

    private String firstName;

    private String lastName;

    private Integer age;

    private String email;

    //@Column(unique=true)
    private String phoneNumber;

    private Double salary;

    private Double commissionRate;

    private String street;

    private String city;

    private String province;

    private String country;

    private String postalCode;

    public Butcher() {
        this.butcherIdentifier = new ButcherIdentifier();
    }

    public Butcher( String firstName, String lastName, Integer age, String email, String phoneNumber, Double salary, Double commissionRate, String street, String city, String province, String country, String postalCode) {
        this.butcherIdentifier = new ButcherIdentifier();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
        this.commissionRate = commissionRate;
        this.street = street;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
    }
}
