package com.butchery.meatservice.datamapperlayer;


import com.butchery.meatservice.datalayer.Meat;
import com.butchery.meatservice.presentationlayer.MeatController;
import com.butchery.meatservice.presentationlayer.MeatResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface MeatResponseMapper {

    @Mapping(expression = "java(meat.getMeatIdentifier().getMeatId())", target = "meatId")
    MeatResponseModel entityToResponseModel(Meat meat);

    List<MeatResponseModel> entityListToResponseModelList(List<Meat> meats);

    @AfterMapping
    default void addLinks(@MappingTarget MeatResponseModel model, Meat meat) {

        //self Link
        Link selfLink = linkTo(methodOn(MeatController.class)
                .getMeatByMeatId(model.getMeatId()))
                .withSelfRel();
        model.add(selfLink);

        Link meatLink = linkTo(methodOn(MeatController.class)
                .getMeats())
                .withRel("All Meats");
        model.add(meatLink);
    }
}
