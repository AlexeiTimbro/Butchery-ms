package com.butchery.purchaseservice.datalayer;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchaseRepository extends MongoRepository<Purchase,String> {

    Purchase findPurchaseByPurchaseIdentifier_PurchaseId(String purchaseId);

    List<Purchase> findAllPurchaseByCustomerIdentifier_CustomerId(String customerId);

    Purchase findPurchaseByCustomerIdentifier_CustomerIdAndPurchaseIdentifier_PurchaseId(String customerId, String purchaseId);

}
