package com.butchery.customerservice.presentationlayer;

import com.butchery.customerservice.datalayer.CustomerRepository;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment= RANDOM_PORT)
@Sql({"/data-mysql.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerControllerTest {

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
    public void whenCustomersExist_thenReturnAllCustomers(){
        // arrange
        Integer expectedNumCustomers =5;

        // act
        webTestClient.get()
                .uri(BASE_URI_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(expectedNumCustomers);

    }

    @Test
    public void whenCreateCustomerWithValidValues_thenReturnNewCustomer(){

        //arrange
        String VALID_FIRST_NAME ="Micheal";
        String VALID_LAST_NAME = "Jordan";
        String VALID_EMAIL = "michealjordan@sciencedirect.com";
        String VALID_PHONE_NUMBER = "514-999-9999";
        String VALID_STREET = "23188 Paris";
        String VALID_CITY = "Paris";
        String VALID_PROVINCE = "Quebec";
        String VALID_COUNTRY = "USA";
        String VALID_POSTAL_CODE = "5R6 5YT";

        CustomerRequestModel customerRequestModel = new CustomerRequestModel(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_PHONE_NUMBER, VALID_STREET, VALID_CITY, VALID_PROVINCE, VALID_COUNTRY, VALID_POSTAL_CODE);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_CUSTOMERS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.customerId").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo(VALID_FIRST_NAME)
                .jsonPath("$.lastName").isEqualTo(VALID_LAST_NAME)
                .jsonPath("$.email").isEqualTo(VALID_EMAIL)
                .jsonPath("$.phoneNumber").isEqualTo(VALID_PHONE_NUMBER)
                .jsonPath("$.street").isEqualTo(VALID_STREET)
                .jsonPath("$.city").isEqualTo(VALID_CITY)
                .jsonPath("$.province").isEqualTo(VALID_PROVINCE)
                .jsonPath("$.country").isEqualTo(VALID_COUNTRY)
                .jsonPath("$.postalCode").isEqualTo(VALID_POSTAL_CODE);
    }

    @Test
    public void whenGetCustomerWithCustomerIdIsValid_thenReturnCustomerWithThatId() {

        //act and assert
        webTestClient.get()
                .uri(BASE_URI_CUSTOMERS + "/" + VALID_CUSTOMER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void whenUpdateCustomerWithValidValues_thenReturnUpdatedCustomer() {



        CustomerRequestModel customerRequestModel = new CustomerRequestModel(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_PHONE_NUMBER, VALID_STREET, VALID_CITY, VALID_PROVINCE, VALID_COUNTRY, VALID_POSTAL_CODE);


        //act and assert
        webTestClient.put()
                .uri(BASE_URI_CUSTOMERS + "/" + VALID_CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.customerId").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo(VALID_FIRST_NAME)
                .jsonPath("$.lastName").isEqualTo(VALID_LAST_NAME)
                .jsonPath("$.email").isEqualTo(VALID_EMAIL)
                .jsonPath("$.phoneNumber").isEqualTo(VALID_PHONE_NUMBER)
                .jsonPath("$.street").isEqualTo(VALID_STREET)
                .jsonPath("$.city").isEqualTo(VALID_CITY)
                .jsonPath("$.province").isEqualTo(VALID_PROVINCE)
                .jsonPath("$.country").isEqualTo(VALID_COUNTRY)
                .jsonPath("$.postalCode").isEqualTo(VALID_POSTAL_CODE);
    }

    @Test
    public void whenDeleteCustomer_thenDeleteCustomer() {

        webTestClient.delete()
                .uri(BASE_URI_CUSTOMERS + "/" + VALID_CUSTOMER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

    }

}