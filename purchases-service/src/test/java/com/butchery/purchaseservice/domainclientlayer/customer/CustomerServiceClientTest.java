package com.butchery.purchaseservice.domainclientlayer.customer;

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
class CustomerServiceClientTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private CustomerServiceClient customerServiceClient;

    private String customerId = "3d16bb8e-5d02-443c-9112-9661282befe1";
    private String baseUrl = "http://localhost:8080/api/v1/customers";


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        customerServiceClient = new CustomerServiceClient(restTemplate, new ObjectMapper(), "localhost", "8080");
    }

    @Test
    void getCustomerByCustomerId() {
        CustomerResponseModel customerResponseModel = new CustomerResponseModel(customerId,"Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");
        when(restTemplate.getForObject(baseUrl + "/" + customerId, CustomerResponseModel.class)).thenReturn(customerResponseModel);

        CustomerResponseModel result = customerServiceClient.getCustomerByCustomerId(customerId);
        assertEquals(result.getCustomerId(), customerId);
        assertEquals(result.getFirstName(), "Joe");
        assertEquals(result.getLastName(), "Burrow");
        assertEquals(result.getEmail(), "joeburrow@gmail.com");
        assertEquals(result.getPhoneNumber(), "514-123-4356");
        assertEquals(result.getStreet(), "street");
        assertEquals(result.getCity(), "city");
        assertEquals(result.getProvince(), "province");
        assertEquals(result.getCountry(), "country");
        assertEquals(result.getPostalCode(), "postalCode");

        verify(restTemplate, times(1)).getForObject(baseUrl + "/" + customerId, CustomerResponseModel.class);

    }

    @Test
    public void testGetCustomerByCustomerId_handleNotFoundException() throws JsonProcessingException {
        String customerId = "This is an invalid customerId";
        HttpErrorInfo errorInfo = new HttpErrorInfo( HttpStatus.NOT_FOUND, "/api/v1/customers/" + customerId,"Not Found");

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

        when(restTemplate.getForObject(baseUrl + "/" + customerId, CustomerResponseModel.class)).thenThrow(ex);

        Exception exception = assertThrows(NotFoundException.class, () ->
                customerServiceClient.getCustomerByCustomerId(customerId));
        assertTrue(exception.getMessage().contains("Not Found"));
    }
}