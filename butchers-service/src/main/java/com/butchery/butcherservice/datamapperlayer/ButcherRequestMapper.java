package com.butchery.butcherservice.datamapperlayer;

import com.butchery.butcherservice.datalayer.Butcher;
import com.butchery.butcherservice.presentationlayer.ButcherRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ButcherRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "butcherIdentifier", ignore = true)
    Butcher requestModelToEntity(ButcherRequestModel butcherRequestModel);
}
