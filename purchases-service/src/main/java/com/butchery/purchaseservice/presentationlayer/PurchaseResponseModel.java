package com.butchery.purchaseservice.presentationlayer;

import com.butchery.purchaseservice.datalayer.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PurchaseResponseModel extends RepresentationModel<PurchaseResponseModel> {

    private String purchaseId;
    private String customerId;
    private String meatId;
    private String butcherId;
    private String butcherFirstName;
    private String butcherLastName;
    private String customerFirstName;
    private String customerLastName;
    private Double salePrice;
    private PurchaseStatus purchaseStatus;
    private String animal;
    private String environment;
    private String texture;
    private String expirationDate;
    private PaymentMethod paymentMethod;
    private LocalDate purchaseDate;
}
