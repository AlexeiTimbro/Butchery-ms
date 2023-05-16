package com.butchery.apigateway.presentationlayer;

import com.butchery.apigateway.domainclientlayer.ButcherServiceClient;
import com.butchery.apigateway.domainclientlayer.PurchaseServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        /*
        String customerIdId = "cusId";

        List<Items> items = new ArrayList<>();

        items.add(new Items("item1", "desc1", 10.0));
        items.add(new Items("item2", "desc2", 20.0));

        OrderRequestModel orderRequestModel = OrderRequestModel.builder()
                .restaurantId("restoId1")
                .menuId("menuId1")
                .totalPrice(20.0)
                .deliveryDriverId("driverId1")
                .orderStatus(OrderStatus.MAKING_ORDER)
                .items(items)
                .estimatedDeliveryTime("10 minutes")
                .orderDate("2023-01-01")
                .build();

        OrderResponseModel orderResponseModel = new OrderResponseModel("orderId1", customerIdId, "restoId1", "menuId1", "driverId1",
                "John", "Doe", "testUser", "test@email.com", items, "resto1", "typeOfMenu1", OrderStatus.MAKING_ORDER, 20.0, "10 minutes", "2023-01-01");

        when(orderServiceClient.processClientOrders(orderRequestModel,customerIdId)).thenReturn(orderResponseModel);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/clients/" + customerIdId + "/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestModel)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);


        OrderResponseModel actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponseModel.class);

        assertEquals(orderRequestModel.getRestaurantId(), actual.getRestaurantId());
        assertEquals(orderRequestModel.getMenuId(), actual.getMenuId());
        assertEquals(orderRequestModel.getTotalPrice(), actual.getFinalPrice());
        assertEquals(orderRequestModel.getDeliveryDriverId(), actual.getDeliveryDriverId());
        assertEquals(orderRequestModel.getOrderStatus(), actual.getOrderStatus());
        assertEquals(orderRequestModel.getItems(), actual.getItems());
        assertEquals(orderRequestModel.getEstimatedDeliveryTime(), actual.getEstimatedDeliveryTime());
        assertEquals(orderRequestModel.getOrderDate(), actual.getOrderDate());

         */
    }

    @Test
    void updateCustomerPurchase() throws Exception{
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