package com.butchery.purchaseservice.businesslayer;

import com.butchery.purchaseservice.presentationlayer.PurchaseRequestModel;
import com.butchery.purchaseservice.presentationlayer.PurchaseResponseModel;

import java.util.List;

public interface PurchaseService {

    List<PurchaseResponseModel> getAllPurchase();
    PurchaseResponseModel processCustomerPurchase(PurchaseRequestModel purchaseRequestModel, String customerId);
}
