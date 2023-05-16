package com.butchery.butcherservice.datamapperlayer;

import com.butchery.butcherservice.datalayer.Butcher;
import com.butchery.butcherservice.presentationlayer.ButcherController;
import com.butchery.butcherservice.presentationlayer.ButcherResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface ButcherResponseMapper {


    @Mapping(expression = "java(butcher.getButcherIdentifier().getButcherId())", target = "butcherId")
    ButcherResponseModel entityToResponseModel(Butcher butcher);

    List<ButcherResponseModel> entityListToResponseModelList(List<Butcher> butchers);


    @AfterMapping
    default void addLinks(@MappingTarget ButcherResponseModel model, Butcher butcher) {

        //self Link
        Link selfLink = linkTo(methodOn(ButcherController.class)
                .getButcherByButcherId(model.getButcherId()))
                .withSelfRel();
        model.add(selfLink);

        Link butcherLink = linkTo(methodOn(ButcherController.class)
                .getButchers())
                .withRel("All Butchers");
        model.add(butcherLink);
    }
}
