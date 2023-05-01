package com.butchery.butcherservice.presentationlayer;

import com.butchery.butcherservice.datalayer.ButcherRepository;
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
class ButcherControllerIntegrationTest {


    private final String BASE_URI_BUTCHERS = "/api/v1/butchers";
    private final String VALID_BUTCHER_ID="3d16bb8e-5d02-443c-9112-9661282befe1";
    private final String VALID_FIRST_NAME ="Bert";
    private final String VALID_LAST_NAME = "Kloss";
    private final Integer VALID_AGE = 26;
    private final String VALID_EMAIL = "bkloss1@sciencedirect.com";
    private final String VALID_PHONE_NUMBER = "180-398-7382";
    private final Double VALID_SALARY = 33896.31;
    private final Double VALID_COMMISSION = 2.3;
    private final String VALID_STREET = "23188 Sunnyside Point";
    private final String VALID_CITY = "Lewisporte";
    private final String VALID_PROVINCE = "Newfoundland and Labrador";
    private final String VALID_COUNTRY = "Canada";
    private final String VALID_POSTAL_CODE = "5R6 28T";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ButcherRepository butcherRepository;


    @Test
    public void whenButchersExist_thenReturnAllButchers(){
        // arrange
        Integer expectedNumButchers =5;

        // act
        webTestClient.get()
                .uri(BASE_URI_BUTCHERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(expectedNumButchers);

    }




    @Test
    public void whenCreateButcherWithValidValues_thenReturnNewButcher(){

        //arrange
          String VALID_FIRST_NAME ="Micheal";
          String VALID_LAST_NAME = "Jordan";
          Integer VALID_AGE = 23;
         String VALID_EMAIL = "michealjordan@sciencedirect.com";
         String VALID_PHONE_NUMBER = "514-999-9999";
         Double VALID_SALARY = 2000000.0;
         Double VALID_COMMISSION = 2.6;
         String VALID_STREET = "23188 Paris";
         String VALID_CITY = "Paris";
         String VALID_PROVINCE = "Quebec";
        String VALID_COUNTRY = "USA";
         String VALID_POSTAL_CODE = "5R6 5YT";

        ButcherRequestModel butcherRequestModel = new ButcherRequestModel(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_AGE, VALID_EMAIL, VALID_PHONE_NUMBER, VALID_SALARY,VALID_COMMISSION,VALID_STREET, VALID_CITY, VALID_PROVINCE, VALID_COUNTRY, VALID_POSTAL_CODE);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_BUTCHERS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(butcherRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.butcherId").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo(VALID_FIRST_NAME)
                .jsonPath("$.lastName").isEqualTo(VALID_LAST_NAME)
                .jsonPath("$.age").isEqualTo(VALID_AGE)
                .jsonPath("$.email").isEqualTo(VALID_EMAIL)
                .jsonPath("$.phoneNumber").isEqualTo(VALID_PHONE_NUMBER)
                .jsonPath("$.salary").isEqualTo(VALID_SALARY)
                .jsonPath("$.commissionRate").isEqualTo(VALID_COMMISSION)
                .jsonPath("$.street").isEqualTo(VALID_STREET)
                .jsonPath("$.city").isEqualTo(VALID_CITY)
                .jsonPath("$.province").isEqualTo(VALID_PROVINCE)
                .jsonPath("$.country").isEqualTo(VALID_COUNTRY)
                .jsonPath("$.postalCode").isEqualTo(VALID_POSTAL_CODE);
    }

    @Test
    public void whenGetButcherWithButcherIdIsValid_thenReturnButcherWithThatId() {

        //act and assert
        webTestClient.get()
                .uri(BASE_URI_BUTCHERS + "/" + VALID_BUTCHER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
                //.jsonPath("$.butcherId").isNotEmpty();
    }



    @Test
    public void whenUpdateButcherWithValidValues_thenReturnUpdatedButcher() {



        ButcherRequestModel butcherRequestModel = new ButcherRequestModel(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_AGE, VALID_EMAIL, VALID_PHONE_NUMBER, VALID_SALARY,VALID_COMMISSION,VALID_STREET, VALID_CITY, VALID_PROVINCE, VALID_COUNTRY, VALID_POSTAL_CODE);


        //act and assert
        webTestClient.put()
                .uri(BASE_URI_BUTCHERS + "/" + VALID_BUTCHER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(butcherRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.butcherId").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo(VALID_FIRST_NAME)
                .jsonPath("$.lastName").isEqualTo(VALID_LAST_NAME)
                .jsonPath("$.age").isEqualTo(VALID_AGE)
                .jsonPath("$.email").isEqualTo(VALID_EMAIL)
                .jsonPath("$.phoneNumber").isEqualTo(VALID_PHONE_NUMBER)
                .jsonPath("$.salary").isEqualTo(VALID_SALARY)
                .jsonPath("$.commissionRate").isEqualTo(VALID_COMMISSION)
                .jsonPath("$.street").isEqualTo(VALID_STREET)
                .jsonPath("$.city").isEqualTo(VALID_CITY)
                .jsonPath("$.province").isEqualTo(VALID_PROVINCE)
                .jsonPath("$.country").isEqualTo(VALID_COUNTRY)
                .jsonPath("$.postalCode").isEqualTo(VALID_POSTAL_CODE);
    }



    @Test
    public void whenDeleteCustomer_thenDeleteCustomer() {

        webTestClient.delete()
                .uri(BASE_URI_BUTCHERS + "/" + VALID_BUTCHER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

    }




}