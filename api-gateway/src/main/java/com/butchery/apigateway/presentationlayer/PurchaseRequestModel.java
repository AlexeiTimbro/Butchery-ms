package com.butchery.apigateway.presentationlayer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Value
public class PurchaseRequestModel {

    String customerId;
    String meatId;
    String butcherId;
    Double salePrice;
    PurchaseStatus purchaseStatus;
    PaymentMethod paymentMethod;
    LocalDate purchaseDate;
}
