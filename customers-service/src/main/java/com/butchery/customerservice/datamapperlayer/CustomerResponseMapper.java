package com.butchery.customerservice.datamapperlayer;

import com.butchery.customerservice.datalayer.Customer;
import com.butchery.customerservice.presentationlayer.CustomerController;
import com.butchery.customerservice.presentationlayer.CustomerResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

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

        //self Link
        Link selfLink = linkTo(methodOn(CustomerController.class)
                .getCustomerByCustomerId(model.getCustomerId()))
                .withSelfRel();
        model.add(selfLink);

        Link customerLink = linkTo(methodOn(CustomerController.class)
                .getCustomers())
                .withRel("All Customers");
        model.add(customerLink);
    }

}
