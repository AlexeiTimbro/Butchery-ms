package com.butchery.purchaseservice.presentationlayer;

import com.butchery.purchaseservice.datalayer.PaymentMethod;
import com.butchery.purchaseservice.datalayer.PurchaseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Value
public class PurchaseRequestModel {

    String purchaseId;
    String customerId;
    String meatId;
    String butcherId;
    Double salePrice;
    PurchaseStatus purchaseStatus;
    PaymentMethod paymentMethod;
    LocalDate purchaseDate;
}
