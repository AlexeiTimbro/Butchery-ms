package com.butchery.butcherservice.utils;

import com.butchery.butcherservice.datalayer.ButcherRepository;
import com.butchery.butcherservice.presentationlayer.ButcherRequestModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment= RANDOM_PORT)
@Sql({"/data-mysql.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GlobalControllerExceptionHandlerTest {

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
    public void whenButcherIdIsInvalidForGet_thenReturnInvalidButcherIdExceptionException() {


        String INVALID_BUTCHER_ID = VALID_BUTCHER_ID + 1;

        webTestClient.get().uri(BASE_URI_BUTCHERS + "/" + INVALID_BUTCHER_ID).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_BUTCHERS + "/" + INVALID_BUTCHER_ID)
                .jsonPath("$.message").isEqualTo("Unknown butcher id");
    }


    @Test
    public void whenButcherIdIsInvalidForUpdate_thenReturnInvalidButcherIdExceptionException() {

        ButcherRequestModel butcherRequestModel = new ButcherRequestModel(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_AGE, VALID_EMAIL, VALID_PHONE_NUMBER, VALID_SALARY,VALID_COMMISSION,VALID_STREET, VALID_CITY, VALID_PROVINCE, VALID_COUNTRY, VALID_POSTAL_CODE);

        String INVALID_BUTCHER_ID = VALID_BUTCHER_ID + 1;
        webTestClient.put()
                .uri(BASE_URI_BUTCHERS + "/" + INVALID_BUTCHER_ID).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(butcherRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_BUTCHERS + "/" + INVALID_BUTCHER_ID)
                .jsonPath("$.message").isEqualTo("Unknown butcher id");


    }


    @Test
    public void whenButcherAgeIsUnder16ForPUT_thenReturnButcherIsTooYoungException() {

        Integer INVALID_AGE = VALID_AGE - 10;

        //arrange
        ButcherRequestModel butcherRequestModel = new ButcherRequestModel(VALID_FIRST_NAME, VALID_LAST_NAME, INVALID_AGE, VALID_EMAIL, VALID_PHONE_NUMBER, VALID_SALARY,VALID_COMMISSION,VALID_STREET, VALID_CITY, VALID_PROVINCE, VALID_COUNTRY, VALID_POSTAL_CODE);

        //act and assert
        webTestClient.put()
                .uri(BASE_URI_BUTCHERS + "/" + VALID_BUTCHER_ID).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(butcherRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_BUTCHERS + "/" + VALID_BUTCHER_ID)
                .jsonPath("$.message").isEqualTo("The age of this butcher is under the minimum requirement of 16.");
    }


    @Test
    public void whenButcherAgeIsUnder16ForPOST_thenReturnButcherIsTooYoungException() {

        Integer INVALID_AGE = VALID_AGE - 10;

        //arrange
        ButcherRequestModel butcherRequestModel = new ButcherRequestModel(VALID_FIRST_NAME, VALID_LAST_NAME, INVALID_AGE, VALID_EMAIL, VALID_PHONE_NUMBER, VALID_SALARY,VALID_COMMISSION,VALID_STREET, VALID_CITY, VALID_PROVINCE, VALID_COUNTRY, VALID_POSTAL_CODE);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_BUTCHERS).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(butcherRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_BUTCHERS)
                .jsonPath("$.message").isEqualTo("The age of this butcher is under the minimum requirement of 16.");
    }


}