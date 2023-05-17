package com.butchery.butcherservice.datamapperlayer;

import com.butchery.butcherservice.datalayer.Butcher;
import com.butchery.butcherservice.presentationlayer.ButcherController;
import com.butchery.butcherservice.presentationlayer.ButcherResponseModel;
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
public interface ButcherResponseMapper {


    @Mapping(expression = "java(butcher.getButcherIdentifier().getButcherId())", target = "butcherId")
    ButcherResponseModel entityToResponseModel(Butcher butcher);

    List<ButcherResponseModel> entityListToResponseModelList(List<Butcher> butchers);


    @AfterMapping
    default void addLinks(@MappingTarget ButcherResponseModel butcherResponseModel, Butcher butcher) {

        URI baseUri = URI.create("http://localhost:8080");

        Link selfLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "butchers", butcherResponseModel.getButcherId())
                        .toUriString(),
                "Self Link");

        Link butcherLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "butchers")
                        .toUriString(),
                "All Butchers");

        butcherResponseModel.add(selfLink);
        butcherResponseModel.add(butcherLink);
    }
}
