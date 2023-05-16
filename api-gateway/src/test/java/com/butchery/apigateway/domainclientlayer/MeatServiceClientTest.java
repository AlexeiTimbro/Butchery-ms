package com.butchery.apigateway.domainclientlayer;

import com.butchery.apigateway.presentationlayer.*;
import com.butchery.apigateway.utils.HttpErrorInfo;
import com.butchery.apigateway.utils.exceptions.NotFoundException;
import com.butchery.apigateway.utils.exceptions.PriceLessOrEqualToZeroException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.List;

import static com.butchery.apigateway.presentationlayer.Status.SOLD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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
    public void getAllButcherTest() {
        // Arrange
        String url = baseUrl;

        MeatResponseModel meat1= new MeatResponseModel("1234", "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);
        MeatResponseModel meat2= new MeatResponseModel("5678", "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);
        MeatResponseModel[] meatResponseModels = new MeatResponseModel[] { meat1, meat2 };

        when(restTemplate.getForObject(url, MeatResponseModel[].class)).thenReturn(meatResponseModels);

        // Act
        List<MeatResponseModel> meatButcherResponseModels = meatServiceClient.getAllMeats();

        // Assert
        assertEquals(meatResponseModels.length, meatButcherResponseModels.size());
        for (int i = 0; i < meatResponseModels.length; i++) {
            assertEquals(meatResponseModels[i], meatButcherResponseModels.get(i));
        }

        verify(restTemplate, times(1)).getForObject(url, MeatResponseModel[].class);

    }

    @Test
    void GetAllMeats_ThrowsHttpClientErrorException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(MeatResponseModel[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act and Assert
        assertThrows(HttpClientErrorException.class, () -> {
            try {
                meatServiceClient.getAllMeats();
            } catch (NotFoundException e) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
        });
    }

    @Test
    public void getMeatByMeatIdTest() {
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
    public void addMeatTest() {
        MeatRequestModel meatRequestModel = MeatRequestModel.builder()
                .animal("animal")
                .status(Status.SOLD)
                .environment("environment")
                .texture("texture")
                .expirationDate("expirationDate")
                .price(20.22)
                .build();

        MeatResponseModel meatResponseModel= new MeatResponseModel(meatId, "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);

        when(restTemplate.postForObject(baseUrl, meatRequestModel, MeatResponseModel.class)).thenReturn(meatResponseModel);

        MeatResponseModel result = meatServiceClient.addMeat(meatRequestModel);

        assertEquals(result.getMeatId(), meatId);

        verify(restTemplate, times(1)).postForObject(baseUrl, meatRequestModel, MeatResponseModel.class);
    }

    @Test
    public void addMeatTest_ThrowsPriceLessOrEqualToZeroException() throws JsonProcessingException{

        MeatRequestModel meatRequestModel = MeatRequestModel.builder()
                .animal("animal")
                .status(Status.SOLD)
                .environment("environment")
                .texture("texture")
                .expirationDate("expirationDate")
                .price(-2.2)
                .build();

        HttpErrorInfo errorInfo = new HttpErrorInfo(HttpStatus.BAD_REQUEST, baseUrl, "The price needs to be higher than 0.");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String errorInfoJson = "";
        try {
            errorInfoJson = objectMapper.writeValueAsString(errorInfo);
        } catch (JsonProcessingException e) {
            fail("Failed to serialize HttpErrorInfo: " + e.getMessage());
        }

        // Create an HttpClientErrorException
        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "The price needs to be higher than 0.",
                HttpHeaders.EMPTY, errorInfoJson.getBytes(), null);

        // Mock the restTemplate call to throw an HttpClientErrorException
        when(restTemplate.postForObject(baseUrl, meatRequestModel, MeatResponseModel.class)).thenThrow(ex);

        // Assert that a HttpClientErrorException is thrown
        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            // Call the method to test
            meatServiceClient.addMeat(meatRequestModel);
        });

        // Verify that restTemplate.postForObject was called
        verify(restTemplate, times(1)).postForObject(baseUrl, meatRequestModel, MeatResponseModel.class);

        // Assert the specific exception type and message
        assertTrue(exception.getMessage().contains("The price needs to be higher than 0."));
    }

    /*
    @Test
    public void addMeatTest_ThisFieldIsRequiredException() throws JsonProcessingException{

        MeatRequestModel meatRequestModel = MeatRequestModel.builder()
                .animal("")
                .status(Status.SOLD)
                .environment("environment")
                .texture("texture")
                .expirationDate("expirationDate")
                .price(-2.2)
                .build();

        HttpErrorInfo errorInfo = new HttpErrorInfo(HttpStatus.BAD_REQUEST, baseUrl, "The price needs to be higher than 0.");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String errorInfoJson = "";
        try {
            errorInfoJson = objectMapper.writeValueAsString(errorInfo);
        } catch (JsonProcessingException e) {
            fail("Failed to serialize HttpErrorInfo: " + e.getMessage());
        }

        // Create an HttpClientErrorException
        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "The price needs to be higher than 0.",
                HttpHeaders.EMPTY, errorInfoJson.getBytes(), null);

        // Mock the restTemplate call to throw an HttpClientErrorException
        when(restTemplate.postForObject(baseUrl, meatRequestModel, MeatResponseModel.class)).thenThrow(ex);

        // Assert that a HttpClientErrorException is thrown
        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            // Call the method to test
            meatServiceClient.addMeat(meatRequestModel);
        });

        // Verify that restTemplate.postForObject was called
        verify(restTemplate, times(1)).postForObject(baseUrl, meatRequestModel, MeatResponseModel.class);

        // Assert the specific exception type and message
        assertTrue(exception.getMessage().contains("The price needs to be higher than 0."));
    }

     */






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
    public void updateMeatTest() {
        String meatId = "id1";

        String url = baseUrl + "/" + meatId;

        MeatRequestModel meatRequestModel = new MeatRequestModel("animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);

        meatServiceClient.updateMeat(meatRequestModel, meatId);

        verify(restTemplate).put(eq(url), eq(meatRequestModel), eq(meatId));

    }

    /*
    @Test
    public void UpdateMeat_NotFoundExceptionTest() throws JsonProcessingException {

        String meatId = "non-existing-id";
        MeatRequestModel meatRequestModel = MeatRequestModel.builder()
                .animal("animal")
                .status(Status.SOLD)
                .environment("environment")
                .texture("texture")
                .expirationDate("expirationDate")
                .price(2.2)
                .build();

        HttpErrorInfo errorInfo = new HttpErrorInfo(HttpStatus.NOT_FOUND, "/api/v1/meats/" + meatId, "Not Found");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String errorInfoJson = objectMapper.writeValueAsString(errorInfo);

        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found",
                HttpHeaders.EMPTY, errorInfoJson.getBytes(), null);

        doThrow(ex).when(restTemplate).execute(anyString(), eq(HttpMethod.PUT), any(), any());

        Exception exception = assertThrows(NotFoundException.class, () ->
                meatServiceClient.updateMeat(meatRequestModel,meatId));

        assertTrue(exception.getMessage().contains("Not Found"));
    }

     */

    @Test
    public void deleteButcherTest() {

        String meatId = "id1";
        String url = baseUrl + "/" + meatId;

        meatServiceClient.deleteMeat(meatId);

        verify(restTemplate).delete(url);
    }




}