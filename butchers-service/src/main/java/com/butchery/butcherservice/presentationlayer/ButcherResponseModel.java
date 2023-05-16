package com.butchery.butcherservice.presentationlayer;

import com.butchery.butcherservice.datalayer.Butcher;
import lombok.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
