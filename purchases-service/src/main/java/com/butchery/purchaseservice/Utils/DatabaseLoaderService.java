package com.butchery.purchaseservice.Utils;

import com.butchery.purchaseservice.datalayer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseLoaderService implements CommandLineRunner {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Override
    public void run(String... args) throws Exception {

        var purchaseIdentifier1 = new PurchaseIdentifier();
        var customerIdentifier1 = new CustomerIdentifier("c3540a89-cb47-4c96-888e-ff96708db4d8");
        var meatIdentifier1 = new MeatIdentifier("9034bbbb-5d02-443c-9112-9661282befe1");
        var butcherIdentifier1 = new ButcherIdentifier("075d5fae-9c99-42f0-91c6-f4ec3d256af9");

        var purchase1 = Purchase.builder()
                .purchaseIdentifier(purchaseIdentifier1)
                .customerIdentifier(customerIdentifier1)
                .meatIdentifier(meatIdentifier1)
                .butcherIdentifier(butcherIdentifier1)
                .butcherFirstName("Lynsey")
                .butcherLastName("Reedie")
                .customerFirstName("Patrick")
                .customerLastName("Bateman")
                .salePrice(20.45)
                .purchaseStatus(PurchaseStatus.PURCHASE_CANCELLED)
                .animal("Chicken")
                .environment("farm")
                .texture("tender")
                .expirationDate("24-08-2024")
                .paymentMethod(PaymentMethod.CREDIT)
                .purchaseDate(LocalDate.of(2023, 04, 10))
                .build();

        //second purchase
        var purchaseIdentifier2 = new PurchaseIdentifier();
        var customerIdentifier2 = new CustomerIdentifier("c4540a89-cb47-4c96-888e-tt96708db4d8");
        var meatIdentifier2 = new MeatIdentifier("4444bbbb-5d02-443c-9112-9661282befe1");
        var butcherIdentifier2 = new ButcherIdentifier("77a89826-3777-4e37-8dd8-6fa31e62790d");

        var purchase2 = Purchase.builder()
                .purchaseIdentifier(purchaseIdentifier2)
                .customerIdentifier(customerIdentifier2)
                .meatIdentifier(meatIdentifier2)
                .butcherIdentifier(butcherIdentifier2)
                .butcherFirstName("Shanan")
                .butcherLastName("Sterzaker")
                .customerFirstName("Low")
                .customerLastName("Tutolar")
                .salePrice(35.45)
                .purchaseStatus(PurchaseStatus.PURCHASE_COMPLETED)
                .animal("Porc")
                .environment("farm")
                .texture("soft")
                .expirationDate("24-08-2024")
                .paymentMethod(PaymentMethod.DEBIT)
                .purchaseDate(LocalDate.of(2023, 06, 12))
                .build();

        purchaseRepository.insert(purchase1);
        purchaseRepository.insert(purchase2);
    }
}

