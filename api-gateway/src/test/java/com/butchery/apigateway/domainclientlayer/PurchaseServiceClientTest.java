package com.butchery.apigateway.domainclientlayer;

import com.butchery.apigateway.presentationlayer.*;
import com.butchery.apigateway.utils.HttpErrorInfo;
import com.butchery.apigateway.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PurchaseServiceClientTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private PurchaseServiceClient purchaseServiceClient;

    private String customerId = "customerId";
    private String baseClientOrderUrl1 = "http://localhost:8080/api/v1/customers/"+customerId+"/purchases";
    private String baseCLientOrderUrl2 = "http://localhost:8080/api/v1/customers";
    private String baseOrderUrl1 = "http://localhost:8080/api/v1/purchases";


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        purchaseServiceClient = new PurchaseServiceClient(restTemplate, new ObjectMapper(), "localhost", "8080");
    }

    @Test
    public void getAllCustomerPurchaseTest() {
        String url = baseClientOrderUrl1;

        PurchaseResponseModel purchase1 = new PurchaseResponseModel("purchaseId", "customerId", "meatId", "butcherId", "butcherFirstName", "butcherLastName",
                "customerFirstName", "customerLastName", 22.22, PurchaseStatus.PURCHASE_COMPLETED, "animal", "environment", "texture", "expirationDate", PaymentMethod.CREDIT, LocalDate.of(2023, 04, 10));
        PurchaseResponseModel purchase2 = new PurchaseResponseModel("purchaseId1", "customerId1", "meatId1", "butcherId1", "butcherFirstName1", "butcherLastName1",
                "customerFirstName1", "customerLastName1", 22.23, PurchaseStatus.PURCHASE_COMPLETED, "animal1", "environment1", "texture1", "expirationDate1", PaymentMethod.DEBIT, LocalDate.of(2024, 04, 10));
        List<PurchaseResponseModel> purchases = List.of(purchase1, purchase2);

        when(restTemplate.getForObject(url, PurchaseResponseModel[].class)).thenReturn(purchases.toArray(new PurchaseResponseModel[0]));

        List<PurchaseResponseModel> actual = purchaseServiceClient.getAllCustomerPurchases(customerId);

        assertEquals(purchases.size(), actual.size());
        for (int i = 0; i < purchases.size(); i++) {
            assertEquals(purchases.get(i), actual.get(i));
        }

        verify(restTemplate, times(1)).getForObject(url, PurchaseResponseModel[].class);
    }

    @Test
    public void getPurchaseByCustomerIdAndPurchaseIdTest() {
        String purchaseId = "purchaseId";

        String url = baseClientOrderUrl1 + "/" + purchaseId;

        PurchaseResponseModel purchase1 = new PurchaseResponseModel("purchaseId", "customerId", "meatId", "butcherId", "butcherFirstName", "butcherLastName",
                "customerFirstName", "customerLastName", 22.22, PurchaseStatus.PURCHASE_COMPLETED, "animal", "environment", "texture", "expirationDate", PaymentMethod.CREDIT, LocalDate.of(2023, 04, 10));

        when(restTemplate.getForObject(url, PurchaseResponseModel.class)).thenReturn(purchase1);

        PurchaseResponseModel actualPurchase = purchaseServiceClient.getPurchaseByCustomerIdAndPurchaseId(customerId,purchaseId);

        assertEquals(purchase1, actualPurchase);

        verify(restTemplate, times(1)).getForObject(url, PurchaseResponseModel.class);
    }

    @Test
    public void getPurchaseByCustomerIdAndPurchaseIdTest_ThrowsException() {

        String purchaseId = "purchaseId";

        String url = baseClientOrderUrl1 + "/" + purchaseId;

        HttpErrorInfo errorInfo = new HttpErrorInfo( HttpStatus.NOT_FOUND, "/api/v1/customers" + customerId + "/purchases/" + purchaseId,"Not Found");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String errorInfoJson = "";
        try {
            errorInfoJson = objectMapper.writeValueAsString(errorInfo);
        } catch (JsonProcessingException e) {
            fail("Failed to serialize HttpErrorInfo: " + e.getMessage());
        }

        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found",
                HttpHeaders.EMPTY, errorInfoJson.getBytes(), null);

        when(restTemplate.getForObject(url, PurchaseResponseModel.class)).thenThrow(ex);

        Exception exception = assertThrows(NotFoundException.class, () ->
                purchaseServiceClient.getPurchaseByCustomerIdAndPurchaseId(customerId,purchaseId));

        assertTrue(exception.getMessage().contains("Not Found"));
    }

    @Test
    public void addPurchaseTest() {
        PurchaseRequestModel purchaseRequestModel = PurchaseRequestModel.builder()
                .purchaseId("purchaseId")
                .customerId("customerId")
                .meatId("meatId")
                .butcherId("butcherId")
                .salePrice(22.22)
                .purchaseStatus(PurchaseStatus.PURCHASE_COMPLETED)
                .paymentMethod(PaymentMethod.CREDIT)
                .purchaseDate(LocalDate.of(2023, 04, 10))
                .build();

        PurchaseResponseModel purchase1 = new PurchaseResponseModel("purchaseId", "customerId", "meatId", "butcherId", "butcherFirstName", "butcherLastName",
                "customerFirstName", "customerLastName", 22.22, PurchaseStatus.PURCHASE_COMPLETED, "animal", "environment", "texture", "expirationDate", PaymentMethod.CREDIT, LocalDate.of(2023, 04, 10));

        String expectedUrl = "http://localhost:8080/api/v1/customers/customerId/purchases";

        when(restTemplate.postForObject(eq(expectedUrl), any(HttpEntity.class), eq(PurchaseResponseModel.class))).thenReturn(purchase1);

        PurchaseResponseModel actual = purchaseServiceClient.addPurchase(purchaseRequestModel, customerId);

        assertNotNull(actual);

        assertEquals(purchase1.getPurchaseId(), actual.getPurchaseId());
        assertEquals(purchase1.getCustomerId(), actual.getCustomerId());
        assertEquals(purchase1.getMeatId(), actual.getMeatId());
        assertEquals(purchase1.getButcherId(), actual.getButcherId());
        assertEquals(purchase1.getSalePrice(), actual.getSalePrice());
        assertEquals(purchase1.getPurchaseStatus(), actual.getPurchaseStatus());
        assertEquals(purchase1.getPaymentMethod(), actual.getPaymentMethod());
        assertEquals(purchase1.getPurchaseDate(), actual.getPurchaseDate());

        verify(restTemplate, times(1)).postForObject(eq(expectedUrl), any(HttpEntity.class), eq(PurchaseResponseModel.class));
    }

    @Test
    public void updatePurchaseTest() {
        String purchaseId = "purchaseId";
        String expectedUrl = "http://localhost:8080/api/v1/customers/customerId/purchases/" + purchaseId;

        PurchaseRequestModel purchaseRequestModel = PurchaseRequestModel.builder()
                .purchaseId("purchaseId")
                .customerId("customerId")
                .meatId("meatId")
                .butcherId("butcherId")
                .salePrice(22.22)
                .purchaseStatus(PurchaseStatus.PURCHASE_COMPLETED)
                .paymentMethod(PaymentMethod.CREDIT)
                .purchaseDate(LocalDate.of(2023, 04, 10))
                .build();

        //when(restTemplate.put(eq(expectedUrl), any(), any(), any())).thenReturn(null);
        when(restTemplate.getForObject(eq(expectedUrl), eq(PurchaseResponseModel.class))).thenReturn(null);

        purchaseServiceClient.updatePurchase(purchaseRequestModel, customerId, purchaseId);

        //verify(restTemplate, times(1)).put(eq(expectedUrl), any(), any(), any());
        verify(restTemplate, times(1)).getForObject(eq(expectedUrl), eq(PurchaseResponseModel.class));
    }

    @Test
    public void deletePurchaseByCustomerIdAndPurchaseIdTest() {
        String purchaseId = "purchaseId";
        String expectedUrl = "http://localhost:8080/api/v1/customers/customerId/purchases/" + purchaseId;

        purchaseServiceClient.deletePurchaseByCustomerIdAndPurchaseId(customerId,purchaseId);

        verify(restTemplate).delete(expectedUrl);

    }




}