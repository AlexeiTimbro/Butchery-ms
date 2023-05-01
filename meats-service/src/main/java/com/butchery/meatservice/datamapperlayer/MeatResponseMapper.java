package com.butchery.meatservice.datamapperlayer;


import com.butchery.meatservice.datalayer.Meat;
import com.butchery.meatservice.presentationlayer.MeatResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MeatResponseMapper {

    @Mapping(expression = "java(meat.getMeatIdentifier().getMeatId())", target = "meatId")
    MeatResponseModel entityToResponseModel(Meat meat);

    List<MeatResponseModel> entityListToResponseModelList(List<Meat> meats);
}
