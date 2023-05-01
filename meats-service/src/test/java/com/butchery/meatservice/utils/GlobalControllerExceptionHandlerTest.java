package com.butchery.meatservice.utils;

import com.butchery.meatservice.datalayer.MeatRepository;
import com.butchery.meatservice.presentationlayer.MeatRequestModel;
import com.butchery.meatservice.utils.exceptions.PriceLessOrEqualToZeroException;
import com.fasterxml.jackson.databind.ser.Serializers;
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

    private final String BASE_URI_MEATS = "/api/v1/meats";
    private final String VALID_MEAT_ID="8098bbbb-5d02-443c-9112-9661282befe1";
    private final String VALID_ANIMAL = "Beef";
    private final String VALID_ENVIRONMENT = "farm";
    private final String VALID_TEXTURE = "tender";
    private final String VALID_EXPIRATION_DATE = "24-08-2024";
    private final Integer VALID_PRICE = 10;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    MeatRepository meatRepository;

    @Test
    public void whenMeatIdIsInvalidForGet_thenReturnInvalidMeatIdExceptionException() {


        String INVALID_MEAT_ID = VALID_MEAT_ID + 1;

        webTestClient.get().uri(BASE_URI_MEATS + "/" + INVALID_MEAT_ID).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_MEATS + "/" + INVALID_MEAT_ID)
                .jsonPath("$.message").isEqualTo("Unknown Meat id.");
    }

    @Test
    public void whenMeatIdIsInvalidForUpdate_thenReturnInvalidMeatIdExceptionException() {

        MeatRequestModel meatRequestModel = new MeatRequestModel(VALID_ANIMAL,VALID_ENVIRONMENT,VALID_TEXTURE,VALID_EXPIRATION_DATE,VALID_PRICE);

        String INVALID_MEAT_ID = VALID_MEAT_ID + 1;

        webTestClient.put()
                .uri(BASE_URI_MEATS + "/" + INVALID_MEAT_ID).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(meatRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_MEATS + "/" + INVALID_MEAT_ID)
                .jsonPath("$.message").isEqualTo("Unknown Meat id.");

    }

    @Test
    public void whenPriceIsLessOrEqualToZeroForPOST_thenReturnPriceIsLessOrEqualToZeroException() {

        Integer INVALID_PRICE = -2;

        //arrange
        MeatRequestModel meatRequestModel = new MeatRequestModel(VALID_ANIMAL,VALID_ENVIRONMENT,VALID_TEXTURE,VALID_EXPIRATION_DATE,INVALID_PRICE);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_MEATS).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(meatRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_MEATS)
                .jsonPath("$.message").isEqualTo("The price needs to be higher then 0.");
    }

    @Test
    public void whenAnimalFieldIsNullForPOST_thenReturnThisFieldIsRequiredException() {

        String INVALID_ANIMAL = null;

        //arrange
        MeatRequestModel meatRequestModel = new MeatRequestModel(INVALID_ANIMAL,VALID_ENVIRONMENT,VALID_TEXTURE,VALID_EXPIRATION_DATE,VALID_PRICE);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_MEATS).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(meatRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_MEATS)
                .jsonPath("$.message").isEqualTo("The animal field is required.");
    }

    @Test
    public void whenEnvironmentFieldIsNullForPOST_thenReturnThisFieldIsRequiredException() {

        String INVALID_ENVIRONMENT = null;

        //arrange
        MeatRequestModel meatRequestModel = new MeatRequestModel(VALID_ANIMAL,INVALID_ENVIRONMENT,VALID_TEXTURE,VALID_EXPIRATION_DATE,VALID_PRICE);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_MEATS).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(meatRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_MEATS)
                .jsonPath("$.message").isEqualTo("The Environment field is required.");
    }

    @Test
    public void whenTextureFieldIsNullForPOST_thenReturnThisFieldIsRequiredException() {

        String INVALID_TEXTURE = null;

        //arrange
        MeatRequestModel meatRequestModel = new MeatRequestModel(VALID_ANIMAL,VALID_ENVIRONMENT,INVALID_TEXTURE,VALID_EXPIRATION_DATE,VALID_PRICE);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_MEATS).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(meatRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_MEATS)
                .jsonPath("$.message").isEqualTo("The Texture field is required.");
    }

    @Test
    public void whenExpirationDateFieldIsNullForPOST_thenReturnThisFieldIsRequiredException() {

        String INVALID_EXPIRATION_DATE = null;

        //arrange
        MeatRequestModel meatRequestModel = new MeatRequestModel(VALID_ANIMAL,VALID_ENVIRONMENT,VALID_TEXTURE,INVALID_EXPIRATION_DATE,VALID_PRICE);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_MEATS).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(meatRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_MEATS)
                .jsonPath("$.message").isEqualTo("The Expiration date field is required.");
    }

    @Test
    public void whenPriceFieldIsNullForPOST_thenReturnThisFieldIsRequiredException() {

        Integer INVALID_PRICE = null;

        //arrange
        MeatRequestModel meatRequestModel = new MeatRequestModel(VALID_ANIMAL,VALID_ENVIRONMENT,VALID_TEXTURE,VALID_EXPIRATION_DATE,INVALID_PRICE);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_MEATS).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(meatRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_MEATS)
                .jsonPath("$.message").isEqualTo("The price field is required.");
    }




}