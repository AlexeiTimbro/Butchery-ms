package com.butchery.meatservice.presentationlayer;

import com.butchery.meatservice.datalayer.Meat;
import com.butchery.meatservice.datalayer.MeatRepository;
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
class MeatControllerTest {

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
    public void whenMeatsExist_thenReturnAllMeats(){
        // arrange
        Integer expectedNumMeats =5;

        // act
        webTestClient.get()
                .uri(BASE_URI_MEATS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(expectedNumMeats);
    }

    @Test
    public void whenCreateMeatWithValidValues_thenReturnNewMeat(){

        //arrange
        String expectedAnimal = "Duck";
        String expectedEnvironment = "Wild";
        String expectedTexture = "Super Tender";
        String expectedExpirationDate = "15-05-2026";
        Integer expectedPrice = 20;

        MeatRequestModel meatRequestModel = new MeatRequestModel(expectedAnimal,expectedEnvironment,expectedTexture,expectedExpirationDate,expectedPrice);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_MEATS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(meatRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.meatId").isNotEmpty()
                .jsonPath("$.animal").isEqualTo(expectedAnimal)
                .jsonPath("$.environment").isEqualTo(expectedEnvironment)
                .jsonPath("$.texture").isEqualTo(expectedTexture)
                .jsonPath("$.expirationDate").isEqualTo(expectedExpirationDate)
                .jsonPath("$.price").isEqualTo(expectedPrice);
    }

    @Test
    public void whenGetMeatWithMeatIdIsValid_thenReturnMeatWithThatId() {

        //act and assert
        webTestClient.get()
                .uri(BASE_URI_MEATS + "/" + VALID_MEAT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void whenUpdateMeatWithValidValues_thenReturnUpdatedMeat() {

        //arrange
        String expectedAnimal = "Duck";
        String expectedEnvironment = "Wild";
        String expectedTexture = "Super Tender";
        String expectedExpirationDate = "15-05-2026";
        Integer expectedPrice = 20;

        MeatRequestModel meatRequestModel = new MeatRequestModel(expectedAnimal,expectedEnvironment,expectedTexture,expectedExpirationDate,expectedPrice);


        //act and assert
        webTestClient.put()
                .uri(BASE_URI_MEATS + "/" + VALID_MEAT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(meatRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.meatId").isNotEmpty()
                .jsonPath("$.animal").isEqualTo(expectedAnimal)
                .jsonPath("$.environment").isEqualTo(expectedEnvironment)
                .jsonPath("$.texture").isEqualTo(expectedTexture)
                .jsonPath("$.expirationDate").isEqualTo(expectedExpirationDate)
                .jsonPath("$.price").isEqualTo(expectedPrice);
    }

    @Test
    public void whenDeleteMeat_thenDeleteMeat() {

        webTestClient.delete()
                .uri(BASE_URI_MEATS + "/" + VALID_MEAT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

}