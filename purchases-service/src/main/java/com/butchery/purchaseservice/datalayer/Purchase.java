package com.butchery.purchaseservice.datalayer;

import com.butchery.purchaseservice.domainclientlayer.customer.CustomerServiceClient;
import com.butchery.purchaseservice.domainclientlayer.meat.MeatServiceClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "purchases")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {

    @Id
    private String id;

    private PurchaseIdentifier purchaseIdentifier;
    private CustomerIdentifier customerIdentifier;
    private MeatIdentifier meatIdentifier;
    private ButcherIdentifier butcherIdentifier;
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
