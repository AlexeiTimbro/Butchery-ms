package com.butchery.apigateway.presentationlayer;

import com.butchery.apigateway.domainclientlayer.CustomerServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CustomersControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    RestTemplate restTemplate;

    @MockBean
    CustomerServiceClient customerServiceClient;

    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate1;


    private ObjectMapper objectMapper = new ObjectMapper();
    private String baseUrlcustomers = "http://localhost:";

    @BeforeEach
    public void setUp() {
        baseUrlcustomers = baseUrlcustomers + port + "api/v1/customers";
    }

    @Test
    void getAllCustomers() throws Exception {

        CustomerResponseModel customer1 = new CustomerResponseModel("1234","Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");
        CustomerResponseModel customer2 = new CustomerResponseModel("5678","Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");

        List<CustomerResponseModel> customerResponseModels = List.of(new CustomerResponseModel[]{customer1, customer2});

        when(customerServiceClient.getAllCustomers()).thenReturn(customerResponseModels);

        mockMvc.perform(get("/api/v1/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerServiceClient, times(1)).getAllCustomers();
    }

    @Test
    void getCustomerByCustomerId() throws Exception{
        String customerId = "id";

        CustomerResponseModel customer1 = new CustomerResponseModel(customerId,"Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");

        when(customerServiceClient.getCustomerByCustomerId(customerId)).thenReturn(customer1);

        mockMvc.perform(get("/api/v1/customers/" + customerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerServiceClient, times(1)).getCustomerByCustomerId(customerId);
    }

    @Test
    void addCustomer() throws Exception{
        String customerId = "id";
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

        CustomerResponseModel customer1 = new CustomerResponseModel(customerId,"Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");

        when(customerServiceClient.addCustomer(customerRequestModel)).thenReturn(customer1);


        MvcResult mvcResult = mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestModel)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        CustomerResponseModel result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerResponseModel.class);
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
    }

    @Test
    void updateCustomer() throws Exception{

        String customerId = "id";
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

        CustomerResponseModel customer1 = new CustomerResponseModel(customerId,"Joe", "Burrow", "joeburrow@gmail.com", "514-123-4356","street", "city", "province","country","postalCode");

        when(customerServiceClient.updateCustomer(customerRequestModel,customerId)).thenReturn(customer1);

        mockMvc.perform(put("/api/v1/customers/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestModel)))
                .andExpect(status().isOk())
                .andReturn();

        verify(customerServiceClient, times(1)).updateCustomer(customerRequestModel,customerId);
    }

    @Test
    void deleteMeat() throws Exception{

        String customerId = "id";

        doNothing().when(customerServiceClient).deleteCustomer(customerId);

        mockMvc.perform(delete("/api/v1/customers/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerServiceClient, times(1)).deleteCustomer(customerId);
    }
}