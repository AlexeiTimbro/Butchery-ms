package com.butchery.apigateway.domainclientlayer;

import com.butchery.apigateway.presentationlayer.ButcherRequestModel;
import com.butchery.apigateway.presentationlayer.ButcherResponseModel;
import com.butchery.apigateway.utils.HttpErrorInfo;
import com.butchery.apigateway.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ButcherServiceClientTest {


    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ButcherServiceClient butcherServiceClient;

    private String butcherId = "3d16bb8e-5d02-443c-9112-9661282befe1";
    private String baseUrl = "http://localhost:8080/api/v1/butchers";


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        butcherServiceClient = new ButcherServiceClient(restTemplate, new ObjectMapper(), "localhost", "8080");
    }

    @Test
    public void getAllButcherTest() {
        // Arrange
        String url = baseUrl;

        ButcherResponseModel butcher1 = new ButcherResponseModel("1234", "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");
        ButcherResponseModel butcher2 = new ButcherResponseModel("5678", "Low", "Blow", 25, "lowblow@gmail.com", "514-123-4356", 55000.00, 5.5, "street0", "city0", "province0","country0","postalCode0");
        ButcherResponseModel[] butcherResponseModels = new ButcherResponseModel[] { butcher1, butcher2 };

        when(restTemplate.getForObject(url, ButcherResponseModel[].class)).thenReturn(butcherResponseModels);

        // Act
        List<ButcherResponseModel> actualButcherResponseModels = butcherServiceClient.getAllButchers();

        // Assert
        assertEquals(butcherResponseModels.length, actualButcherResponseModels.size());
        for (int i = 0; i < butcherResponseModels.length; i++) {
            assertEquals(butcherResponseModels[i], actualButcherResponseModels.get(i));
        }

        verify(restTemplate, times(1)).getForObject(url, ButcherResponseModel[].class);
    }


    @Test
    public void getButcherByButcherIdTest() {
        ButcherResponseModel butcherResponseModel = new ButcherResponseModel(butcherId, "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");
        when(restTemplate.getForObject(baseUrl + "/" + butcherId, ButcherResponseModel.class)).thenReturn(butcherResponseModel);

        ButcherResponseModel result = butcherServiceClient.getButcherByButcherId(butcherId);
        assertEquals(result.getButcherId(), butcherId);
        assertEquals(result.getFirstName(), "Joe");
        assertEquals(result.getLastName(), "Burrow");
        assertEquals(result.getAge(), 21);
        assertEquals(result.getEmail(), "joeburrow@gmail.com");
        assertEquals(result.getPhoneNumber(), "514-123-4356");
        assertEquals(result.getSalary(), 45000.00);
        assertEquals(result.getCommissionRate(), 4.5);
        assertEquals(result.getStreet(), "street");
        assertEquals(result.getCity(), "city");
        assertEquals(result.getProvince(), "province");
        assertEquals(result.getCountry(), "country");
        assertEquals(result.getPostalCode(), "postalCode");

        verify(restTemplate, times(1)).getForObject(baseUrl + "/" + butcherId, ButcherResponseModel.class);

    }

    @Test
    public void addButcherTest() {
        ButcherRequestModel butcherRequestModel = ButcherRequestModel.builder()
                .firstName("Joe")
                .lastName("Burrow")
                .age(21)
                .email("joeburrow@gmail.com")
                .phoneNumber("514-123-4356")
                .salary(45000.00)
                .commissionRate(4.5)
                .street("street")
                .city("city")
                .province("province")
                .country("country")
                .postalCode("postalCode")
                .build();

        ButcherResponseModel butcherResponseModel = new ButcherResponseModel(butcherId, "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");

        when(restTemplate.postForObject(baseUrl, butcherRequestModel, ButcherResponseModel.class)).thenReturn(butcherResponseModel);

        ButcherResponseModel result = butcherServiceClient.addButcher(butcherRequestModel);
        assertEquals(result.getButcherId(), butcherId);
        assertEquals(result.getFirstName(), "Joe");
        assertEquals(result.getLastName(), "Burrow");
        assertEquals(result.getAge(), 21);
        assertEquals(result.getEmail(), "joeburrow@gmail.com");
        assertEquals(result.getPhoneNumber(), "514-123-4356");
        assertEquals(result.getSalary(), 45000.00);
        assertEquals(result.getCommissionRate(), 4.5);
        assertEquals(result.getStreet(), "street");
        assertEquals(result.getCity(), "city");
        assertEquals(result.getProvince(), "province");
        assertEquals(result.getCountry(), "country");
        assertEquals(result.getPostalCode(), "postalCode");

        verify(restTemplate, times(1)).postForObject(baseUrl, butcherRequestModel, ButcherResponseModel.class);
    }

    @Test
    public void testGetButcherByButcherId_handleNotFoundException() throws JsonProcessingException {
        String butcherId = "This is an invalid butcherId";
        HttpErrorInfo errorInfo = new HttpErrorInfo( HttpStatus.NOT_FOUND, "/api/v1/butchers/" + butcherId,"Not Found");

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

        when(restTemplate.getForObject(baseUrl + "/" + butcherId, ButcherResponseModel.class)).thenThrow(ex);

        Exception exception = assertThrows(NotFoundException.class, () ->
                butcherServiceClient.getButcherByButcherId(butcherId));

        assertTrue(exception.getMessage().contains("Not Found"));
    }


    /*
    @Test
    public void updateButcherTest() {

        // Arrange
        String url = baseUrl + "/" + butcherId;
        ButcherRequestModel butcherRequestModel = new ButcherRequestModel("Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");

        ButcherResponseModel expectedResponse = new ButcherResponseModel("1234", "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province", "country", "postalCode");

        when(restTemplate.execute(eq(url), eq(HttpMethod.PUT), any(), any()))
                .thenReturn(expectedResponse);

        // Act
        ButcherResponseModel result = butcherServiceClient.updateButcher(butcherRequestModel, butcherId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getButcherId(), result.getButcherId());
        assertEquals(expectedResponse.getFirstName(), result.getFirstName());
        assertEquals(expectedResponse.getLastName(), result.getLastName());
        assertEquals(expectedResponse.getAge(), result.getAge());
        assertEquals(expectedResponse.getEmail(), result.getEmail());
        assertEquals(expectedResponse.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(expectedResponse.getSalary(), result.getSalary());
        assertEquals(expectedResponse.getCommissionRate(), result.getCommissionRate());
        assertEquals(expectedResponse.getStreet(), result.getStreet());
        assertEquals(expectedResponse.getCity(), result.getCity());
        assertEquals(expectedResponse.getProvince(), result.getProvince());
        assertEquals(expectedResponse.getCountry(), result.getCountry());
        assertEquals(expectedResponse.getPostalCode(), result.getPostalCode());

        verify(restTemplate, times(1)).execute(eq(url), eq(HttpMethod.PUT), any(), any());
    }

     */

}





