package com.butchery.apigateway.presentationlayer;

import com.butchery.apigateway.domainclientlayer.ButcherServiceClient;
import com.butchery.apigateway.domainclientlayer.PurchaseServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;



import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CustomerPurchaseControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    RestTemplate restTemplate;

    @MockBean
    PurchaseServiceClient purchaseServiceClient;
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate1;


    private ObjectMapper objectMapper = new ObjectMapper();
    private String baseUrlButchers = "http://localhost:";
    private String customerId = "customerId";

    @BeforeEach
    public void setUp() {
        baseUrlButchers = baseUrlButchers + port + "api/v1/customers/";
    }

    @Test
    void getAllCustomerPurchases() throws Exception{
        String customerId = "id";
        List<PurchaseResponseModel> purchases = List.of(new PurchaseResponseModel[]{});

        when(purchaseServiceClient.getAllCustomerPurchases(customerId)).thenReturn(purchases);

        mockMvc.perform(get("/api/v1/customers/" + customerId + "/purchases"))
                .andExpect(status().isOk());

        verify(purchaseServiceClient, times(1)).getAllCustomerPurchases(customerId);
    }

    @Test
    void getPurchaseByCustomerIdAndPurchaseId() throws Exception{

        String customerId = "cusId";
        String purchaseId = "purId";
        PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();

        when(purchaseServiceClient.getPurchaseByCustomerIdAndPurchaseId(customerId,purchaseId)).thenReturn(purchaseResponseModel);

        mockMvc.perform(get("/api/v1/customers/" + customerId + "/purchases/" + purchaseId))
                .andExpect(status().isOk());

        verify(purchaseServiceClient, times(1)).getPurchaseByCustomerIdAndPurchaseId(customerId,purchaseId);
    }

    @Test
    void processCustomerPurchase() throws Exception{
        String customerId = "customerId";

        objectMapper.registerModule(new JavaTimeModule());

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

        when(purchaseServiceClient.addPurchase(purchaseRequestModel, customerId)).thenReturn(purchase1);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/customers/" + customerId + "/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseRequestModel)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        PurchaseResponseModel actual = objectMapper.readValue(responseContent, PurchaseResponseModel.class);

        assertEquals(purchase1.getPurchaseId(), actual.getPurchaseId());
        assertEquals(purchase1.getCustomerId(), actual.getCustomerId());
        assertEquals(purchase1.getMeatId(), actual.getMeatId());
        assertEquals(purchase1.getButcherId(), actual.getButcherId());
        assertEquals(purchase1.getSalePrice(), actual.getSalePrice());
        assertEquals(purchase1.getPurchaseStatus(), actual.getPurchaseStatus());
        assertEquals(purchase1.getPaymentMethod(), actual.getPaymentMethod());
        assertEquals(purchase1.getPurchaseDate(), actual.getPurchaseDate());
    }

    @Test
    void updateCustomerPurchase() throws Exception{

            String customerId = "customerId";
            String purchaseId = "purId";

        objectMapper.registerModule(new JavaTimeModule());

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

        when(purchaseServiceClient.updatePurchase(purchaseRequestModel,customerId,purchaseId)).thenReturn(purchase1);

        mockMvc.perform(put("/api/v1/customers/" + customerId + "/purchases/" + purchaseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseRequestModel)))
                .andExpect(status().isOk())
                .andReturn();

        verify(purchaseServiceClient, times(1)).updatePurchase(purchaseRequestModel,customerId,purchaseId);
        }


    @Test
    void deletePurchaseByCustomerIdAndPurchaseId() throws Exception{

        String customerId = "cusId";
        String purchaseId = "purId";

        doNothing().when(purchaseServiceClient).deletePurchaseByCustomerIdAndPurchaseId(customerId,purchaseId);

        mockMvc.perform(delete("/api/v1/customers/" + customerId + "/purchases/" + purchaseId))
                .andExpect(status().isNoContent());

        verify(purchaseServiceClient, times(1)).deletePurchaseByCustomerIdAndPurchaseId(customerId,purchaseId);
    }
}