package com.butchery.purchaseservice.datamappinglayer;

import com.butchery.purchaseservice.datalayer.Purchase;
import com.butchery.purchaseservice.presentationlayer.CustomerPurchaseController;
import com.butchery.purchaseservice.presentationlayer.PurchaseResponseModel;
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
public interface PurchaseResponseModelMapper {

    @Mapping(expression = "java(purchase.getPurchaseIdentifier().getPurchaseId())", target = "purchaseId")
    @Mapping(expression = "java(purchase.getButcherIdentifier().getButcherId())", target = "butcherId")
    @Mapping(expression = "java(purchase.getCustomerIdentifier().getCustomerId())", target = "customerId")
    @Mapping(expression = "java(purchase.getMeatIdentifier().getMeatId())", target = "meatId")
    PurchaseResponseModel entityToResponseModel(Purchase purchase);

    List<PurchaseResponseModel> entityListToResponseModelList(List<Purchase> purchases);

    @AfterMapping
    default void addLinks(@MappingTarget PurchaseResponseModel purchaseResponseModel, Purchase purchase) {

        URI baseUri = URI.create("http://localhost:8080");

        Link selfLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "customers", purchaseResponseModel.getCustomerId(), "purchases", purchaseResponseModel.getPurchaseId())
                        .toUriString(),
                "Self link");

        Link purchaseLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "customers", purchaseResponseModel.getCustomerId(), "purchases")
                        .toUriString(),
                "All Purchases");

        purchaseResponseModel.add(selfLink);
        purchaseResponseModel.add(purchaseLink);
    }


}

