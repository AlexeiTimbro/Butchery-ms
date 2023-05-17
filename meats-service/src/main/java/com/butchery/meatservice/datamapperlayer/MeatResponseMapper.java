package com.butchery.meatservice.datamapperlayer;

import com.butchery.meatservice.datalayer.Meat;
import com.butchery.meatservice.presentationlayer.MeatController;
import com.butchery.meatservice.presentationlayer.MeatResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface MeatResponseMapper {

    @Mapping(expression = "java(meat.getMeatIdentifier().getMeatId())", target = "meatId")
    MeatResponseModel entityToResponseModel(Meat meat);

    List<MeatResponseModel> entityListToResponseModelList(List<Meat> meats);

    @AfterMapping
    default void addLinks(@MappingTarget MeatResponseModel meatResponseModel, Meat meat) {

        URI baseUri = URI.create("http://localhost:8080");

        Link selfLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "meats", meatResponseModel.getMeatId())
                        .toUriString(),
                "Self Link");

        Link meatLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "meats")
                        .toUriString(),
                "All Meats");

        meatResponseModel.add(selfLink);
        meatResponseModel.add(meatLink);
    }
}
