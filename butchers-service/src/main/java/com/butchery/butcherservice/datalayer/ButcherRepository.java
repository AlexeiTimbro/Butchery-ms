package com.butchery.butcherservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ButcherRepository extends JpaRepository<Butcher, Integer> {

    //List<Butcher> findAllButchersByPurchaseIdentifier_PurchaseId(String purchaseId);
    //Butcher findByPurchaseIdentifier_PurchaseIdAndButcherIdentifier_ButcherId(String purchaseId, String butcherId);
    Butcher findButcherByButcherIdentifier_ButcherId(String ButcherId);

    List<Butcher> findButcherByPhoneNumber(String phoneNumber);

    Boolean existsByButcherIdentifier_ButcherId(String butcherId);


    //Butcher findAllButchersByCustomerIdentifier_CustomerId(String customerId);

}
