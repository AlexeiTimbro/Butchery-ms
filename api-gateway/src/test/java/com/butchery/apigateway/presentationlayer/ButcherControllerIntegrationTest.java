package com.butchery.apigateway.presentationlayer;

import com.butchery.apigateway.domainclientlayer.ButcherServiceClient;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ButcherControllerIntegrationTest {

    private String port = "8080/";

    @Autowired
    RestTemplate restTemplate;

    @MockBean
    ButcherServiceClient butcherServiceClient;
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate1;


    private ObjectMapper objectMapper = new ObjectMapper();
    private String baseUrlButchers = "http://localhost:";

    @BeforeEach
    public void setUp() {
        baseUrlButchers = baseUrlButchers + port + "api/v1/butchers";
    }

    @Test
    void getAllButchers() throws Exception {

        ButcherResponseModel butcher1 = new ButcherResponseModel("1234", "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");
        ButcherResponseModel butcher2 = new ButcherResponseModel("5678", "Low", "Blow", 25, "lowblow@gmail.com", "514-123-4356", 55000.00, 5.5, "street0", "city0", "province0","country0","postalCode0");
        List<ButcherResponseModel> butcherResponseModels = List.of(new ButcherResponseModel[]{butcher1, butcher2});

        when(butcherServiceClient.getAllButchers()).thenReturn(butcherResponseModels);

        mockMvc.perform(get("/api/v1/butchers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(butcherServiceClient, times(1)).getAllButchers();
    }

    @Test
    void getButcherByButcherId() throws Exception {
        String butcherId = "id";

        ButcherResponseModel butcher1 = new ButcherResponseModel(butcherId, "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");

        when(butcherServiceClient.getButcherByButcherId(butcherId)).thenReturn(butcher1);

        mockMvc.perform(get("/api/v1/butchers/" + butcherId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(butcherServiceClient, times(1)).getButcherByButcherId(butcherId);
    }

    @Test
    public void returnAllButchers() throws Exception {

        String butcherId = "id";

        ButcherResponseModel butcher1 = new ButcherResponseModel(butcherId, "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");

        when(butcherServiceClient.getButcherByButcherId(butcherId)).thenReturn(butcher1);
        assertEquals(butcher1.getButcherId(), butcherId);
        assertEquals(butcher1.getFirstName(), "Joe");
        assertEquals(butcher1.getLastName(), "Burrow");
        assertEquals(butcher1.getAge(), 21);
        assertEquals(butcher1.getEmail(), "joeburrow@gmail.com");
        assertEquals(butcher1.getPhoneNumber(), "514-123-4356");
        assertEquals(butcher1.getSalary(), 45000.00);
        assertEquals(butcher1.getCommissionRate(), 4.5);
        assertEquals(butcher1.getStreet(), "street");
        assertEquals(butcher1.getCity(), "city");
        assertEquals(butcher1.getProvince(), "province");
        assertEquals(butcher1.getCountry(), "country");
        assertEquals(butcher1.getPostalCode(), "postalCode");
    }

    @Test
    void addButcher() throws Exception {
        String butcherId = "id";
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

        ButcherResponseModel butcher1 = new ButcherResponseModel(butcherId, "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");

        when(butcherServiceClient.addButcher(butcherRequestModel)).thenReturn(butcher1);


        MvcResult mvcResult = mockMvc.perform(post("/api/v1/butchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(butcherRequestModel)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        ButcherResponseModel result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ButcherResponseModel.class);
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
    }

    @Test
    public void updateButcher() throws Exception {

        String butcherId = "id";
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

        ButcherResponseModel butcher1 = new ButcherResponseModel(butcherId, "Joe", "Burrow", 21, "joeburrow@gmail.com", "514-123-4356", 45000.00, 4.5, "street", "city", "province","country","postalCode");

        when(butcherServiceClient.updateButcher(butcherRequestModel,butcherId)).thenReturn(butcher1);

        mockMvc.perform(put("/api/v1/butchers/" + butcherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(butcherRequestModel)))
                .andExpect(status().isOk())
                .andReturn();

        verify(butcherServiceClient, times(1)).updateButcher(butcherRequestModel, butcherId);
    }

    @Test
    void deleteButcher() throws Exception {
        String butcherId = "id";

        doNothing().when(butcherServiceClient).deleteButcher(butcherId);

        mockMvc.perform(delete("/api/v1/butchers/" + butcherId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(butcherServiceClient, times(1)).deleteButcher(butcherId);
    }
}