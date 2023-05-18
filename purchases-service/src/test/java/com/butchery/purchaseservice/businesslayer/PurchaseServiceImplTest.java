package com.butchery.purchaseservice.businesslayer;

import com.butchery.purchaseservice.datalayer.*;
import com.butchery.purchaseservice.datamappinglayer.PurchaseResponseModelMapper;
import com.butchery.purchaseservice.domainclientlayer.butcher.ButcherResponseModel;
import com.butchery.purchaseservice.domainclientlayer.butcher.ButcherServiceClient;
import com.butchery.purchaseservice.domainclientlayer.customer.CustomerResponseModel;
import com.butchery.purchaseservice.domainclientlayer.customer.CustomerServiceClient;
import com.butchery.purchaseservice.domainclientlayer.meat.MeatResponseModel;
import com.butchery.purchaseservice.domainclientlayer.meat.MeatServiceClient;
import com.butchery.purchaseservice.domainclientlayer.meat.Status;
import com.butchery.purchaseservice.presentationlayer.PurchaseRequestModel;
import com.butchery.purchaseservice.presentationlayer.PurchaseResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class PurchaseServiceImplTest {

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

    @Test
    void getAllCustomerPurchases() {

        String customerId = "customerId";

        Purchase purchase1 = buildPurchase1();
        Purchase purchase2 = buildPurchase1();

        List<Purchase> purchases = new ArrayList<>(Arrays.asList(purchase1, purchase2));

        when(purchaseRepository.findAllPurchaseByCustomerIdentifier_CustomerId(customerId)).thenReturn(purchases);

        List<PurchaseResponseModel> purchaseResponseModels = purchaseService.getAllCustomerPurchases(customerId);

        assertNotNull(purchaseResponseModels);
        assertEquals(2, purchaseResponseModels.size());

        verify(purchaseRepository, times(1)).findAllPurchaseByCustomerIdentifier_CustomerId(customerId);
        verify(purchaseResponseModelMapper, times(1)).entityListToResponseModelList(purchases);
    }

    @Test
    void getCustomerPurchaseByCustomerAndPurchaseId() {
        String customerId = "customerId";
        String purchaseId = "purchaseId";

        Purchase purchase = buildPurchase2();

        when(purchaseRepository.findPurchaseByCustomerIdentifier_CustomerIdAndPurchaseIdentifier_PurchaseId(customerId,purchaseId)).thenReturn(purchase);

        PurchaseResponseModel purchaseResponseModel = purchaseService.getCustomerPurchaseByCustomerAndPurchaseId(customerId, purchaseId);

        assertNotNull(purchaseResponseModel);
        assertEquals(purchaseId, purchaseResponseModel.getPurchaseId());

        verify(purchaseRepository, times(1)).findPurchaseByCustomerIdentifier_CustomerIdAndPurchaseIdentifier_PurchaseId(customerId,purchaseId);
        verify(purchaseResponseModelMapper, times(1)).entityToResponseModel(purchase);
    }

    @Test
    void updateCustomerPurchase() {

        String customerId = "customerId";
        String purchaseId = "aee3b186-9699-42d8-ad47-5fd3b2c63b7a";

        CustomerResponseModel customerResponseModel = new CustomerResponseModel(customerId, "Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");

        MeatResponseModel meatResponseModel= new MeatResponseModel("meatId", "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);

        ButcherResponseModel butcherResponseModel = new ButcherResponseModel("butcherId", "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");


        PurchaseRequestModel purchaseRequestModel = PurchaseRequestModel.builder()
                .customerId("customerId")
                .meatId("meatId")
                .butcherId("butcherId")
                .salePrice(22.22)
                .purchaseStatus(PurchaseStatus.PURCHASE_COMPLETED)
                .paymentMethod(PaymentMethod.CREDIT)
                .purchaseDate(LocalDate.of(2023, 04, 10))
                .build();


        Purchase existingPurchase = buildPurchase2();

        when(purchaseRepository.findPurchaseByPurchaseIdentifier_PurchaseId(purchaseId)).thenReturn(existingPurchase);
        when(customerServiceClient.getCustomerByCustomerId(customerId)).thenReturn(customerResponseModel);
        when(meatServiceClient.getMeatByMeatId(purchaseRequestModel.getMeatId())).thenReturn(meatResponseModel);
        when(butcherServiceClient.getButcherByButcherId(purchaseRequestModel.getButcherId())).thenReturn(butcherResponseModel);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(existingPurchase);

        // Act
        PurchaseResponseModel purchaseResponseModel = purchaseService.updateCustomerPurchase(purchaseRequestModel,customerId,purchaseId);

        // Assert
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

        verify(purchaseResponseModelMapper, times(1)).entityToResponseModel(existingPurchase);

    }

    @Test
    void deleteCustomerPurchaseByCustomerAndPurchaseId() {
        String customerId = "customerId";
        String purchaseId = "purchaseId";

        Purchase purchase = buildPurchase2();

        when(purchaseRepository.findPurchaseByCustomerIdentifier_CustomerIdAndPurchaseIdentifier_PurchaseId(customerId,purchaseId)).thenReturn(purchase);
        doNothing().when(purchaseRepository).delete(purchase);

        purchaseService.deleteCustomerPurchaseByCustomerAndPurchaseId(customerId, purchaseId);

        verify(purchaseRepository, times(1)).findPurchaseByCustomerIdentifier_CustomerIdAndPurchaseIdentifier_PurchaseId(customerId,purchaseId);
        verify(purchaseRepository, times(1)).delete(purchase);
    }


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

    private Purchase buildPurchase2() {

        var purchaseIdentifier1 = new PurchaseIdentifier("purchaseId");
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