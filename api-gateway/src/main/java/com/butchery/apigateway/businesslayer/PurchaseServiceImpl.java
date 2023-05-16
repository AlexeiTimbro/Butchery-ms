package com.butchery.apigateway.businesslayer;

import com.butchery.apigateway.domainclientlayer.MeatServiceClient;
import com.butchery.apigateway.domainclientlayer.PurchaseServiceClient;
import com.butchery.apigateway.presentationlayer.MeatRequestModel;
import com.butchery.apigateway.presentationlayer.MeatResponseModel;
import com.butchery.apigateway.presentationlayer.PurchaseRequestModel;
import com.butchery.apigateway.presentationlayer.PurchaseResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceImpl implements  PurchaseService{

    private PurchaseServiceClient purchaseServiceClient;

    public PurchaseServiceImpl(PurchaseServiceClient purchaseServiceClient) {
        this.purchaseServiceClient = purchaseServiceClient;
    }

    /*
    @Override
    public List<PurchaseResponseModel> getAllPurchasesAggregate() {
        return purchaseServiceClient.getAllPurchases();
    }

     */

    @Override
    public List<PurchaseResponseModel> getAllCustomerPurchases(String customerId) {
        return purchaseServiceClient.getAllCustomerPurchases(customerId);
    }

    /*
    @Override
    public PurchaseResponseModel getPurchaseByPurchaseIdAggregate(String purchaseId) {
        return purchaseServiceClient.getPurchaseByPurchaseId(purchaseId);
    }

     */

    @Override
    public PurchaseResponseModel getCustomerPurchaseByCustomerAndPurchaseId(String customerId, String purchaseId) {
        return purchaseServiceClient.getPurchaseByCustomerIdAndPurchaseId(customerId,purchaseId);
    }

    @Override
    public PurchaseResponseModel processCustomerPurchase(PurchaseRequestModel purchaseRequestModel, String customerId) {
        return purchaseServiceClient.addPurchase(purchaseRequestModel,customerId);
    }

    @Override
    public PurchaseResponseModel updateCustomerPurchase(PurchaseRequestModel purchaseRequestModel, String customerId, String purchaseId) {
        return purchaseServiceClient.updatePurchase(purchaseRequestModel,customerId,purchaseId);
    }

    /*
    @Override
    public void deletePurchaseAggregate(String purchaseId) {
        purchaseServiceClient.deletePurchase(purchaseId);
    }
    */

    @Override
    public void deleteCustomerPurchaseByCustomerAndPurchaseId(String customerId, String purchaseId) {
        purchaseServiceClient.deletePurchaseByCustomerIdAndPurchaseId(customerId,purchaseId);
    }

}

