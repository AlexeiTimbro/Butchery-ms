package com.butchery.purchaseservice.presentationlayer;

import com.butchery.purchaseservice.Utils.HttpErrorInfo;
import com.butchery.purchaseservice.datalayer.MeatIdentifier;
import com.butchery.purchaseservice.datalayer.PaymentMethod;
import com.butchery.purchaseservice.datalayer.PurchaseRepository;
import com.butchery.purchaseservice.datalayer.PurchaseStatus;
import com.butchery.purchaseservice.domainclientlayer.butcher.ButcherResponseModel;
import com.butchery.purchaseservice.domainclientlayer.customer.CustomerResponseModel;
import com.butchery.purchaseservice.domainclientlayer.meat.MeatRequestModel;
import com.butchery.purchaseservice.domainclientlayer.meat.MeatResponseModel;
import com.butchery.purchaseservice.domainclientlayer.meat.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerPurchaseControllerIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockRestServiceServer;
    private ObjectMapper mapper = new ObjectMapper();

    private String customerId = "c3540a89-cb47-4c96-888e-ff96708db4d8";
    private UUID purchaseId;

    private final String BASE_URI_CUSTOMER_PURCHASES = "/api/v1/customers/" + customerId + "/purchases";


    @Autowired
    PurchaseRepository purchaseRepository;

    @BeforeEach
    public void init(){
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void getAllCustomerPurchases() {

        /*

        Integer expectedNumPurchases =5;

        // act
        webTestClient.get()
                .uri(BASE_URI_CUSTOMER_PURCHASES + "/" + purchaseId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(expectedNumPurchases);

         *
    }

    @Test
    void getAllCustomerPurchaseByCustomerIdAndPurchaseId() {

        /*
        webTestClient.get().uri(BASE_URI_CUSTOMER_PURCHASES + "/" + purchaseId).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
                .jsonPath("$.meatId").isEqualTo(VALID_MEAT_ID)
                .jsonPath("$.animal").isEqualTo(VALID_ANIMAL)
                .jsonPath("$.environment").isEqualTo(VALID_ENVIRONMENT)
                .jsonPath("$.texture").isEqualTo(VALID_TEXTURE)
                .jsonPath("$.expirationDate").isEqualTo(VALID_EXPIRATION_DATE)
                .jsonPath("$.price").isEqualTo(VALID_PRICE);


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

         */
    }

    @Test
    void processCustomerPurchase() {
    }

    @Test
    void updateCustomerPurchase() {
    }

    @Test
    void removeCustomerPurchase() {

/*
        webTestClient.delete().uri(BASE_URI_CUSTOMER_PURCHASES + "/" + purchaseId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isNoContent();

        String uri =BASE_URI_CUSTOMER_PURCHASES + "/" + purchaseId;
        webTestClient.get()
                .uri(BASE_URI_CUSTOMER_PURCHASES + "/" + purchaseId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND)
                .expectBody(HttpErrorInfo.class).value((dto) -> {
                            assertNotNull(dto.getMessage());
                            assertEquals(dto.getPath(), "uri=/" + uri);
                        }
                );

        webTestClient.delete()
                .uri(BASE_URI_CUSTOMER_PURCHASES + "/" + purchaseId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
*/

    }

    @Test
    public void whenFieldsAreValid_theReturnPurchaseResponseModel() throws JsonProcessingException, URISyntaxException {

        /*
        //arrange
        PurchaseRequestModel purchaseRequestModel = PurchaseRequestModel.builder()
                .customerId("c3540a89-cb47-4c96-888e-ff96708db4d8")
                .meatId("9034bbbb-5d02-443c-9112-9661282befe1")
                .butcherId("77a89826-3777-4e37-8dd8-6fa31e62790d")
                .salePrice(22.22)
                .purchaseStatus(PurchaseStatus.PURCHASE_COMPLETED)
                .paymentMethod(PaymentMethod.CREDIT)
                .purchaseDate(LocalDate.of(2023, 04, 10))
                .build();

        String customerId = "c3540a89-cb47-4c96-888e-ff96708db4d8";

        CustomerResponseModel customerResponseModel = new CustomerResponseModel(customerId, "Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:7001/api/v1/customers/c3540a89-cb47-4c96-888e-ff96708db4d8")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(customerResponseModel)));


        MeatResponseModel meatResponseModel= new MeatResponseModel("9034bbbb-5d02-443c-9112-9661282befe1", "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:7002/api/v1/meats/9034bbbb-5d02-443c-9112-9661282befe1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(meatResponseModel)));

        MeatRequestModel meatRequestModel = MeatRequestModel.builder()
                .animal("animal")
                .status(Status.SOLD)
                .environment("environment")
                .texture("texture")
                .expirationDate("expirationDate")
                .price(20.22)
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:7002/api/v1/meats/9034bbbb-5d02-443c-9112-9661282befe1")))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json(mapper.writeValueAsString(meatRequestModel)))
                .andRespond(withStatus(HttpStatus.OK));



        ButcherResponseModel butcherResponseModel = new ButcherResponseModel("77a89826-3777-4e37-8dd8-6fa31e62790d", "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:7003/api/v1/butchers/77a89826-3777-4e37-8dd8-6fa31e62790d")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(butcherResponseModel)));


        //act and assert
        String url = "api/v1/customers/" + customerId + "/purchases";
        webTestClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(purchaseRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PurchaseResponseModel.class)
                .value((purchaseResponseModel) -> {
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
                });

         */

    }
}