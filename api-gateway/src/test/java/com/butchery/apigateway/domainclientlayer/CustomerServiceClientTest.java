package com.butchery.apigateway.domainclientlayer;

import com.butchery.apigateway.presentationlayer.ButcherRequestModel;
import com.butchery.apigateway.presentationlayer.ButcherResponseModel;
import com.butchery.apigateway.presentationlayer.CustomerRequestModel;
import com.butchery.apigateway.presentationlayer.CustomerResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
    public void getAllCustomerTest() {
        // Arrange
        String url = baseUrl;

        CustomerResponseModel customer1 = new CustomerResponseModel("1234", "Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");
        CustomerResponseModel customer2 = new CustomerResponseModel("5678", "Low", "Blow","lowblow@gmail.com", "514-123-4356","street0", "city0", "province0","country0","postalCode0");
        CustomerResponseModel[] customerResponseModels = new CustomerResponseModel[] { customer1, customer2};

        when(restTemplate.getForObject(url, CustomerResponseModel[].class)).thenReturn(customerResponseModels);

        // Act
        List<CustomerResponseModel> actualCustomerResponseModels = customerServiceClient.getAllCustomers();

        // Assert
        assertEquals(customerResponseModels.length, actualCustomerResponseModels.size());
        for (int i = 0; i < customerResponseModels.length; i++) {
            assertEquals(customerResponseModels[i], actualCustomerResponseModels.get(i));
        }

        verify(restTemplate, times(1)).getForObject(url, CustomerResponseModel[].class);
    }


    @Test
    public void getCustomerByCustomerIdTest() {
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
    public void addCustomerTest() {

        // Create a mock RestTemplate
        RestTemplate restTemplate = mock(RestTemplate.class);

        // Create a mock CustomerRequestModel
        CustomerRequestModel customerRequestModel = new CustomerRequestModel("Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");
        // Set customerRequestModel properties for testing

        // Create a mock CustomerResponseModel
        CustomerResponseModel expectedResponse = new CustomerResponseModel();
        // Set expectedResponse properties for testing

        // Create a mock HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a mock HttpEntity
        HttpEntity<CustomerRequestModel> requestEntity = new HttpEntity<>(customerRequestModel, headers);

        // Mock the restTemplate.postForObject method to return the expected response
        when(restTemplate.postForObject(anyString(), eq(requestEntity), eq(CustomerResponseModel.class)))
                .thenReturn(expectedResponse);

        // Invoke the addCustomer method
        CustomerResponseModel actualResponse = customerServiceClient.addCustomer(customerRequestModel);

        // Verify the restTemplate.postForObject method was called with the correct arguments
        verify(restTemplate).postForObject(eq(baseUrl), eq(requestEntity), eq(CustomerResponseModel.class));

        // Verify the actualResponse matches the expectedResponse
        assertEquals(expectedResponse, actualResponse);
    }

}