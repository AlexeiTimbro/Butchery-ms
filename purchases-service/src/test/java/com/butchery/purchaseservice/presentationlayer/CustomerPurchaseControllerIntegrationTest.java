package com.butchery.purchaseservice.presentationlayer;

import com.butchery.purchaseservice.businesslayer.PurchaseService;
import com.butchery.purchaseservice.datalayer.*;
import com.butchery.purchaseservice.datamappinglayer.PurchaseResponseModelMapper;
import com.butchery.purchaseservice.domainclientlayer.butcher.ButcherResponseModel;
import com.butchery.purchaseservice.domainclientlayer.butcher.ButcherServiceClient;
import com.butchery.purchaseservice.domainclientlayer.customer.CustomerResponseModel;
import com.butchery.purchaseservice.domainclientlayer.customer.CustomerServiceClient;
import com.butchery.purchaseservice.domainclientlayer.meat.MeatResponseModel;
import com.butchery.purchaseservice.domainclientlayer.meat.MeatServiceClient;
import com.butchery.purchaseservice.domainclientlayer.meat.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class CustomerPurchaseControllerIntegrationTest {


    @Autowired
    PurchaseService purchaseService;

    @MockBean
    CustomerServiceClient customerServiceClient;

    @MockBean
    ButcherServiceClient butcherServiceClient;

    @MockBean
    MeatServiceClient meatServiceClient;

    @MockBean
    PurchaseRepository purchaseRepository;

    @SpyBean
    PurchaseResponseModelMapper purchaseResponseModelMapper;

/*
    @Test
    void getAllCustomerPurchases() {
    }

    @Test
    void getAllCustomerPurchaseByCustomerIdAndPurchaseId() {


    }

    @Test
    void updateCustomerPurchase() {
    }

    @Test
    void removeCustomerPurchase() {
    }

 */

    @Test
    void whenValidCustomerId_MeatId_ButcherId_thenProcessCustomerPurchase_ShouldSucceed(){

        //arrange
        PurchaseRequestModel purchaseRequestModel = PurchaseRequestModel.builder()
                .customerId("customerId")
                .meatId("meatId")
                .butcherId("butcherId")
                .salePrice(22.22)
                .purchaseStatus(PurchaseStatus.PURCHASE_COMPLETED)
                .paymentMethod(PaymentMethod.CREDIT)
                .purchaseDate(LocalDate.of(2023, 04, 10))
                .build();

        String customerId = "customerId";

        CustomerResponseModel customerResponseModel = new CustomerResponseModel(customerId, "Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");

        MeatResponseModel meatResponseModel= new MeatResponseModel("1234", "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);

        ButcherResponseModel butcherResponseModel = new ButcherResponseModel("5678", "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");



        //required for purchaseOrder repo mock
        Purchase purchase = buildPurchase1();
        Purchase saved = buildPurchase1();
        saved.setId("0001");


        //define mock behaviors
        when(customerServiceClient.getCustomerByCustomerId(customerId)).thenReturn(customerResponseModel);
        when(butcherServiceClient.getButcherByButcherId(purchaseRequestModel.getButcherId())).thenReturn(butcherResponseModel);
        when(meatServiceClient.getMeatByMeatId(purchaseRequestModel.getMeatId())).thenReturn(meatResponseModel);

        when(purchaseRepository.save(any(Purchase.class))).thenReturn(saved);


        //act (if you dont have an act you havent tested anything)
        PurchaseResponseModel purchaseResponseModel = purchaseService.processCustomerPurchase(purchaseRequestModel, customerId);

        //assert
        assertNotNull(purchaseResponseModel);
        assertNotNull(purchaseResponseModel.getPurchaseId());
        assertEquals(purchaseRequestModel.getMeatId(), purchaseResponseModel.getMeatId());
        assertEquals(customerId, purchaseResponseModel.getCustomerId());
        assertEquals(purchaseRequestModel.getButcherId(), purchaseResponseModel.getButcherId());
        assertEquals(butcherResponseModel.getFirstName(), purchaseResponseModel.getButcherFirstName());
        assertEquals(butcherResponseModel.getLastName(), purchaseResponseModel.getButcherLastName());
        assertEquals(customerResponseModel.getFirstName(), purchaseResponseModel.getCustomerFirstName());
        assertEquals(customerResponseModel.getLastName(), purchaseResponseModel.getCustomerLastName());
        assertEquals(purchaseRequestModel.getSalePrice(), purchaseResponseModel.getSalePrice());
        assertEquals(purchaseRequestModel.getPurchaseStatus(), purchaseResponseModel.getPurchaseStatus());
        assertEquals(meatResponseModel.getAnimal(), purchaseResponseModel.getAnimal());
        assertEquals(meatResponseModel.getEnvironment(), purchaseResponseModel.getEnvironment());
        assertEquals(meatResponseModel.getTexture(), purchaseResponseModel.getTexture());
        assertEquals(meatResponseModel.getExpirationDate(), purchaseResponseModel.getExpirationDate());
        assertEquals(purchaseRequestModel.getPaymentMethod(), purchaseResponseModel.getPaymentMethod());
        assertEquals(purchaseRequestModel.getPurchaseDate(), purchaseResponseModel.getPurchaseDate());

        //for the spy
        verify(purchaseResponseModelMapper, times(1)).entityToResponseModel(saved);


    }

    private Purchase buildPurchase1() {

        var purchaseIdentifier1 = new PurchaseIdentifier();
        var customerIdentifier1 = new CustomerIdentifier("customerId");
        var meatIdentifier1 = new MeatIdentifier("meatId");
        var butcherIdentifier1 = new ButcherIdentifier("butcherId");

        var purchase1 = Purchase.builder()
                .purchaseIdentifier(purchaseIdentifier1)
                .customerIdentifier(customerIdentifier1)
                .meatIdentifier(meatIdentifier1)
                .butcherIdentifier(butcherIdentifier1)
                .butcherFirstName("Joe")
                .butcherLastName("Burrow")
                .customerFirstName("Joe")
                .customerLastName("Burrow")
                .salePrice(22.22)
                .purchaseStatus(PurchaseStatus.PURCHASE_COMPLETED)
                .animal("animal")
                .environment("environment")
                .texture("texture")
                .expirationDate("expirationDate")
                .paymentMethod(PaymentMethod.CREDIT)
                .purchaseDate(LocalDate.of(2023, 04, 10))
                .build();
        return purchase1;
    }


}