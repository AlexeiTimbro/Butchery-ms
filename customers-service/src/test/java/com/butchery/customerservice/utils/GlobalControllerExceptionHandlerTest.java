package com.butchery.customerservice.utils;

import com.butchery.customerservice.datalayer.CustomerRepository;
import com.butchery.customerservice.presentationlayer.CustomerRequestModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment= RANDOM_PORT)
@Sql({"/data-mysql.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GlobalControllerExceptionHandlerTest {

    private final String BASE_URI_CUSTOMERS = "/api/v1/customers";
    private final String VALID_CUSTOMER_ID="1146d776-3017-4747-b61b-7d6fa95a3839";
    private final String VALID_FIRST_NAME ="Erling";
    private final String VALID_LAST_NAME = "Haaland";
    private final String VALID_EMAIL = "erlinghaaland@prweb.com";
    private final String VALID_PHONE_NUMBER = "505-646-1333";
    private final String VALID_STREET = "76246 Beilfuss Road";
    private final String VALID_CITY = "Marisol";
    private final String VALID_PROVINCE = "Setúbal";
    private final String VALID_COUNTRY = "Portugal";
    private final String VALID_POSTAL_CODE = "5R6 28T";


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void whenCustomerIdIsInvalidForGet_thenReturnInvalidCustomerIdExceptionException() {


        String INVALID_CUSTOMER_ID = VALID_CUSTOMER_ID + 1;
        webTestClient.get().uri(BASE_URI_CUSTOMERS + "/" + INVALID_CUSTOMER_ID).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_CUSTOMERS + "/" + INVALID_CUSTOMER_ID)
                .jsonPath("$.message").isEqualTo("Unknown Customer id");
    }

    @Test
    public void whenCustomerIdIsInvalidForUpdate_thenReturnInvalidCustomerIdExceptionException() {

        CustomerRequestModel customerRequestModel = new CustomerRequestModel(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_PHONE_NUMBER, VALID_STREET, VALID_CITY, VALID_PROVINCE, VALID_COUNTRY, VALID_POSTAL_CODE);

        String INVALID_CUSTOMER_ID = VALID_CUSTOMER_ID + 1;

        webTestClient.put()
                .uri(BASE_URI_CUSTOMERS + "/" + INVALID_CUSTOMER_ID).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_CUSTOMERS + "/" + INVALID_CUSTOMER_ID)
                .jsonPath("$.message").isEqualTo("Unknown Customer id");

    }


    @Test
    public void whenCustomerPhoneNumberIsDuplicatedForPOST_thenReturnDuplicatePhoneNumberException() {

        String DUPLICATED_PHONE_NUMBER = VALID_PHONE_NUMBER;

        //arrange
        CustomerRequestModel customerRequestModel = new CustomerRequestModel(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, DUPLICATED_PHONE_NUMBER, VALID_STREET, VALID_CITY, VALID_PROVINCE, VALID_COUNTRY, VALID_POSTAL_CODE);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_CUSTOMERS).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_CUSTOMERS)
                .jsonPath("$.message").isEqualTo("This phone number is already in use by another customer.");
    }

}