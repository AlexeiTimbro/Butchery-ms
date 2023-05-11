package com.butchery.purchaseservice.datamappinglayer;

import com.butchery.purchaseservice.datalayer.Purchase;
import com.butchery.purchaseservice.presentationlayer.PurchaseResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseResponseModelMapper {

    @Mapping(expression = "java(purchase.getPurchaseIdentifier().getPurchaseId())", target = "purchaseId")
    @Mapping(expression = "java(purchase.getButcherIdentifier().getButcherId())", target = "butcherId")
    @Mapping(expression = "java(purchase.getCustomerIdentifier().getCustomerId())", target = "customerId")
    @Mapping(expression = "java(purchase.getMeatIdentifier().getMeatId())", target = "meatId")
    PurchaseResponseModel entityToResponseModel(Purchase purchase);

    List<PurchaseResponseModel> entityListToResponseModelList(List<Purchase> purchases);
}
