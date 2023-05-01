package com.butchery.meatservice.datamapperlayer;


import com.butchery.meatservice.datalayer.Meat;
import com.butchery.meatservice.presentationlayer.MeatRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeatRequestMapper {

    /*
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(expression = "java(meatIdentifier)", target = "meatIdentifier"),
            @Mapping(expression = "java(purchaseIdentifier)", target = "purchaseIdentifier")
    })
    Meat requestModelToEntity(MeatRequestModel meatRequestModel, MeatIdentifier meatIdentifier, PurchaseIdentifier purchaseIdentifier);

     */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "meatIdentifier", ignore = true)
    Meat requestModelToEntity1(MeatRequestModel meatRequestModel);
}
