package com.butchery.purchaseservice.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseLoaderService implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {

    }
/*

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public void run(String... args) throws Exception {

        var purchaseIdentifier1 = new PurchaseOrderIdentifier();
        var vehicleIdentifier1 = new VehicleIdentifier("JN8AS5MTXDW375966");
        var clientIdentifier1 = new ClientIdentifier("c3540a89-cb47-4c96-888e-ff96708db4d8");
        var employeeIdentifier1 = new EmployeeIdentifier("efaa2e89-9e09-4183-a509-a8a65eb55495");
        var inventoryIdentifier1 = new InventoryIdentifier("d846a5a7-2e1c-4c79-809c-4f3f471e826d");

        var financingAgreement1 = FinancingAgreementDetails.builder()
                .monthlyPaymentAmount(858.34)
                .numberOfMonthlyPayments(60)
                .downPaymentAmount(15000.00)
                .build();

        var purchaseOrder1 = PurchaseOrder.builder()
                .purchaseOrderIdentifier(purchaseIdentifier1)
                .vehicleIdentifier(vehicleIdentifier1)
                .clientIdentifier(clientIdentifier1)
                .employeeIdentifier(employeeIdentifier1)
                .inventoryIdentifier(inventoryIdentifier1)
                .employeeFirstName("Dorry")
                .employeeLastName("Gepp")
                .clientFirstName("Alick")
                .clientLastName("Ucceli")
                .salePrice(66500.00)
                .status(Status.PURCHASE_COMPLETED)
                .financingAgreementDetails(financingAgreement1)
                .purchaseOfferDate(LocalDate.of(2023, 04, 10))
                .vehicleMake("Nissan")
                .vehicleModel("Rogue")
                .build();

        //second purchase order
        var purchaseIdentifier2=new PurchaseOrderIdentifier();
        var vehicleIdentifier2=new VehicleIdentifier("WBAYA8C50FG933061");
        var clientIdentifier2=new ClientIdentifier("dd1ab8b0-ab17-4e03-b70a-84caa3871606");
        var employeeIdentifier2=new EmployeeIdentifier("6d8e5f5b-8350-40ed-ac06-e484498f4f41");
        var inventoryIdentifier2=new InventoryIdentifier("3fe5c169-c1ef-42ea-9e5e-870f30ba9dd0");

        var financingAgreement2=FinancingAgreementDetails.builder()
                .monthlyPaymentAmount(1833.34)
                .numberOfMonthlyPayments(18)
                .downPaymentAmount(10000.00)
                .build();

        var purchaseOrder2=PurchaseOrder.builder()
                .purchaseOrderIdentifier(purchaseIdentifier2)
                .vehicleIdentifier(vehicleIdentifier2)
                .clientIdentifier(clientIdentifier2)
                .employeeIdentifier(employeeIdentifier2)
                .inventoryIdentifier(inventoryIdentifier2)
                .employeeFirstName("Vikki")
                .employeeLastName("Wymer")
                .clientFirstName("Rickie")
                .clientLastName("Presslie")
                .salePrice(43000.00)
                .status(Status.PURCHASE_NEGOTIATION)
                .financingAgreementDetails(financingAgreement2)
                .purchaseOfferDate(LocalDate.of(2023,04,18))
                .vehicleMake("BMW")
                .vehicleModel("750 / ALPINA B7")
                .build();

        purchaseOrderRepository.insert(purchaseOrder1);
        purchaseOrderRepository.insert(purchaseOrder2);
    }

 */
}
