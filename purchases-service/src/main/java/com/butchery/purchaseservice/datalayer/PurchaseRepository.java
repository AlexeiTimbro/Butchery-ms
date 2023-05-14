package com.butchery.purchaseservice.datalayer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseRepository extends MongoRepository<Purchase,String> {

    Purchase findPurchaseByPurchaseIdentifier_PurchaseId(String purchaseId);
}
