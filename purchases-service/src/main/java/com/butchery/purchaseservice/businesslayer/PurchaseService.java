package com.butchery.purchaseservice.businesslayer;

import com.butchery.purchaseservice.presentationlayer.PurchaseRequestModel;
import com.butchery.purchaseservice.presentationlayer.PurchaseResponseModel;

import java.util.List;

public interface PurchaseService {

    //List<PurchaseResponseModel> getAllPurchaseAggregate();

    List<PurchaseResponseModel> getAllCustomerPurchases(String customerId);

    //PurchaseResponseModel getPurchaseByPurchaseIdAggregate(String purchaseId);

    PurchaseResponseModel getCustomerPurchaseByCustomerAndPurchaseId(String customerId,String purchaseId);

    PurchaseResponseModel processCustomerPurchase(PurchaseRequestModel purchaseRequestModel, String customerId);

    PurchaseResponseModel updateCustomerPurchase(PurchaseRequestModel purchaseRequestModel,String customerId,String purchaseId);

    //void deletePurchaseAggregate(String purchaseId);

    void deleteCustomerPurchaseByCustomerAndPurchaseId(String customerId,String purchaseId);

}
