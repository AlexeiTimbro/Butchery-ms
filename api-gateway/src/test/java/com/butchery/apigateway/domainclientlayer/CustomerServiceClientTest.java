package com.butchery.apigateway.domainclientlayer;

import com.butchery.apigateway.presentationlayer.ButcherRequestModel;
import com.butchery.apigateway.presentationlayer.ButcherResponseModel;
import com.butchery.apigateway.presentationlayer.CustomerRequestModel;
import com.butchery.apigateway.presentationlayer.CustomerResponseModel;
import com.butchery.apigateway.utils.HttpErrorInfo;
import com.butchery.apigateway.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
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
    void GetAllCustomers_ThrowsHttpClientErrorException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(CustomerResponseModel[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act and Assert
        assertThrows(HttpClientErrorException.class, () -> {
            try {
                customerServiceClient.getAllCustomers();
            } catch (NotFoundException e) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
        });
    }

    @Test
    public void addCustomerTest() {
        CustomerRequestModel customerRequestModel = CustomerRequestModel.builder()
                .firstName("Joe")
                .lastName("Burrow")
                .email("joeburrow@gmail.com")
                .phoneNumber("514-123-4356")
                .street("street")
                .city("city")
                .province("province")
                .country("country")
                .postalCode("postalCode")
                .build();

        CustomerResponseModel customerResponseModel = new CustomerResponseModel(customerId,"Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");

        when(restTemplate.postForObject(baseUrl, customerRequestModel, CustomerResponseModel.class)).thenReturn(customerResponseModel);

        CustomerResponseModel result = customerServiceClient.addCustomer(customerRequestModel);

        assertEquals(result.getCustomerId(), customerId);

        verify(restTemplate, times(1)).postForObject(baseUrl, customerRequestModel, CustomerResponseModel.class);
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

    @Test
    public void updateCustomerTest() {
        String customerId = "id1";
        String url = baseUrl + "/" + customerId;

        CustomerRequestModel customerRequestModel = new CustomerRequestModel("Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");

        when(restTemplate.execute(eq(url), eq(HttpMethod.PUT), any(RequestCallback.class), any())).thenReturn(null);

        customerServiceClient.updateCustomer(customerRequestModel,customerId);

        verify(restTemplate, times(1)).execute(eq(url), eq(HttpMethod.PUT), any(RequestCallback.class), any());
    }

    @Test
    public void deleteCustomerTest() {
        String customerId = "id1";
        String url = baseUrl + "/" + customerId;

        when(restTemplate.execute(eq(url), eq(HttpMethod.DELETE), any(),  any())).thenReturn(null);

        customerServiceClient.deleteCustomer(customerId);

        verify(restTemplate, times(1)).execute(eq(url), eq(HttpMethod.DELETE), any(),  any());
    }


    @Test
    public void callBackTest() throws Exception {
        CustomerRequestModel customerRequestModel = CustomerRequestModel.builder()
                .firstName("Joe")
                .lastName("Burrow")
                .email("joeburrow@gmail.com")
                .phoneNumber("514-123-4356")
                .street("street")
                .city("city")
                .province("province")
                .country("country")
                .postalCode("postalCode")
                .build();

        ClientHttpRequest clientHttpRequest = mock(ClientHttpRequest.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(clientHttpRequest.getBody()).thenReturn(outputStream);
        HttpHeaders httpHeaders = new HttpHeaders();
        when(clientHttpRequest.getHeaders()).thenReturn(httpHeaders);

        Method requestCallbackMethod = CustomerServiceClient.class.getDeclaredMethod("requestCallback", CustomerRequestModel.class);
        requestCallbackMethod.setAccessible(true);

        RequestCallback requestCallback = (RequestCallback) requestCallbackMethod.invoke(customerServiceClient, customerRequestModel);
        requestCallback.doWithRequest(clientHttpRequest);

        ObjectMapper mapper = new ObjectMapper();
        String expectedBody = mapper.writeValueAsString(customerRequestModel);
        String actualBody = outputStream.toString();
        assertEquals(expectedBody, actualBody);

        assertEquals(MediaType.APPLICATION_JSON_VALUE, httpHeaders.getContentType().toString());
        assertTrue(httpHeaders.getAccept().contains(MediaType.APPLICATION_JSON));
    }




}