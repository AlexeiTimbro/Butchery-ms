package com.butchery.customerservice.datamapperlayer;

import com.butchery.customerservice.datalayer.Customer;
import com.butchery.customerservice.presentationlayer.CustomerRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CustomerRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerIdentifier", ignore = true)
    Customer requestModelToEntity(CustomerRequestModel customerRequestModel);

}
