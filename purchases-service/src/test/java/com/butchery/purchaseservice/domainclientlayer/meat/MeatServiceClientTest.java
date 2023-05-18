package com.butchery.purchaseservice.domainclientlayer.meat;

import com.butchery.purchaseservice.Utils.Exceptions.NotFoundException;
import com.butchery.purchaseservice.Utils.HttpErrorInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MeatServiceClientTest {


    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private MeatServiceClient meatServiceClient;

    private String meatId = "3d16bb8e-5d02-443c-9112-9661282befe1";
    private String baseUrl = "http://localhost:8080/api/v1/meats";


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        meatServiceClient = new MeatServiceClient(restTemplate, new ObjectMapper(), "localhost", "8080");
    }

    @Test
    void getMeatByMeatId() {
        MeatResponseModel meatResponseModel= new MeatResponseModel(meatId, "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);
        when(restTemplate.getForObject(baseUrl + "/" + meatId, MeatResponseModel.class)).thenReturn(meatResponseModel);

        MeatResponseModel result = meatServiceClient.getMeatByMeatId(meatId);
        assertEquals(result.getMeatId(), meatId);
        assertEquals(result.getAnimal(), "animal");
        assertEquals(result.getStatus(), Status.SOLD);
        assertEquals(result.getEnvironment(), "environment");
        assertEquals(result.getTexture(), "texture");
        assertEquals(result.getExpirationDate(), "expirationDate");
        assertEquals(result.getPrice(), 20.22);

        verify(restTemplate, times(1)).getForObject(baseUrl + "/" + meatId, MeatResponseModel.class);

    }

    @Test
    public void testGetMeatByMeatId_handleNotFoundException() throws JsonProcessingException {
        String meatId = "Invalid meatId";
        HttpErrorInfo errorInfo = new HttpErrorInfo( HttpStatus.NOT_FOUND, "/api/v1/meats/" + meatId,"Not Found");

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

        when(restTemplate.getForObject(baseUrl + "/" + meatId, MeatResponseModel.class)).thenThrow(ex);

        Exception exception = assertThrows(NotFoundException.class, () ->
                meatServiceClient.getMeatByMeatId(meatId));

        assertTrue(exception.getMessage().contains("Not Found"));
    }
    @Test
    void updateMeatStatus() {
    }
}