package com.butchery.meatservice.datalayer;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="meats")
@Data
public class Meat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private MeatIdentifier meatIdentifier;

    private String animal;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String environment;
    private String texture;
    private String expirationDate;
    private Double price;


    public Meat(){

    }

    public Meat(String animal, Status status, String environment, String texture, String expirationDate, Double price) {
        this.meatIdentifier = new MeatIdentifier();
        this.animal = animal;
        this.status = status;
        this.environment = environment;
        this.texture = texture;
        this.expirationDate = expirationDate;
        this.price = price;
    }
}
