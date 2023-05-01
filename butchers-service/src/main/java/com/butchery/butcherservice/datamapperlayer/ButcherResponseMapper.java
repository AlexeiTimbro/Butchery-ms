package com.butchery.butcherservice.datamapperlayer;

import com.butchery.butcherservice.datalayer.Butcher;
import com.butchery.butcherservice.presentationlayer.ButcherResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ButcherResponseMapper {


    @Mapping(expression = "java(butcher.getButcherIdentifier().getButcherId())", target = "butcherId")
    ButcherResponseModel entityToResponseModel(Butcher butcher);

    List<ButcherResponseModel> entityListToResponseModelList(List<Butcher> butchers);
}
