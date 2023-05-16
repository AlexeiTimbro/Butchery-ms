package com.butchery.customerservice.datamapperlayer;

import com.butchery.customerservice.datalayer.Customer;
import com.butchery.customerservice.presentationlayer.CustomerController;
import com.butchery.customerservice.presentationlayer.CustomerResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface CustomerResponseMapper {


    @Mapping(expression = "java(customer.getCustomerIdentifier().getCustomerId())", target = "customerId")
    CustomerResponseModel entityToResponseModel(Customer customer);

    List<CustomerResponseModel> entityListToResponseModelList(List<Customer> customers);

    @AfterMapping
    default void addLinks(@MappingTarget CustomerResponseModel model, Customer customer) {

        URI baseUri = URI.create("http://localhost:8080");
/*
        Link selfLink = link.of(methodOn(CustomerController.class)
                .getCustomerByCustomerId(model.getCustomerId()))
                .withSelfRel();
        model.add(selfLink);

        Link selfLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "clients", clientResponseModel.getClientId())
                        .toUriString(),
                "self");

        Link customerLink = linkTo(methodOn(CustomerController.class)
                .getCustomers())
                .withRel("All Customers");
        model.add(customerLink);

 */
    }

}
