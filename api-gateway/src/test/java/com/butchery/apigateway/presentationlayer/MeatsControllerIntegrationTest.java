package com.butchery.apigateway.presentationlayer;

import com.butchery.apigateway.domainclientlayer.MeatServiceClient;
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
class MeatsControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    RestTemplate restTemplate;

    @MockBean
    MeatServiceClient meatServiceClient;

    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate1;


    private ObjectMapper objectMapper = new ObjectMapper();
    private String baseUrlMeats = "http://localhost:";

    @BeforeEach
    public void setUp() {
        baseUrlMeats = baseUrlMeats + port + "api/v1/meats";
    }

    @Test
    void getAllMeats() throws Exception{

        MeatResponseModel meat1= new MeatResponseModel("1234", "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);
        MeatResponseModel meat2= new MeatResponseModel("5678", "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);
        List<MeatResponseModel> meatResponseModels = List.of(new MeatResponseModel[]{meat1, meat2});

        when(meatServiceClient.getAllMeats()).thenReturn(meatResponseModels);

        mockMvc.perform(get("/api/v1/meats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(meatServiceClient, times(1)).getAllMeats();
    }

    @Test
    void getMeatByMeatId() throws Exception{

        String meatId = "id";

        MeatResponseModel meat1= new MeatResponseModel(meatId, "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);

        when(meatServiceClient.getMeatByMeatId(meatId)).thenReturn(meat1);

        mockMvc.perform(get("/api/v1/meats/" + meatId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(meatServiceClient, times(1)).getMeatByMeatId(meatId);
    }

    @Test
    void addMeat() throws Exception{

        String meatId = "id";
        MeatRequestModel meatRequestModel = MeatRequestModel.builder()
                .animal("animal")
                .status(Status.SOLD)
                .environment("environment")
                .texture("texture")
                .expirationDate("expirationDate")
                .price(20.22)
                .build();

        MeatResponseModel meat1= new MeatResponseModel(meatId, "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);

        when(meatServiceClient.addMeat(meatRequestModel)).thenReturn(meat1);


        MvcResult mvcResult = mockMvc.perform(post("/api/v1/meats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meatRequestModel)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        MeatResponseModel result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MeatResponseModel.class);
        assertEquals(result.getMeatId(), meatId);
        assertEquals(result.getAnimal(), "animal");
        assertEquals(result.getStatus(), Status.SOLD);
        assertEquals(result.getEnvironment(), "environment");
        assertEquals(result.getTexture(), "texture");
        assertEquals(result.getExpirationDate(), "expirationDate");
        assertEquals(result.getPrice(), 20.22);
    }

    @Test
    void updateMeat() throws Exception{
        String meatId = "id";
        MeatRequestModel meatRequestModel = MeatRequestModel.builder()
                .animal("animal")
                .status(Status.SOLD)
                .environment("environment")
                .texture("texture")
                .expirationDate("expirationDate")
                .price(20.22)
                .build();


        MeatResponseModel meat1= new MeatResponseModel(meatId, "animal", Status.SOLD,"environment", "texture",  "expirationDate", 20.22);

        when(meatServiceClient.updateMeat(meatRequestModel,meatId)).thenReturn(meat1);

        mockMvc.perform(put("/api/v1/meats/" + meatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meatRequestModel)))
                .andExpect(status().isOk())
                .andReturn();

        verify(meatServiceClient, times(1)).updateMeat(meatRequestModel,meatId);
    }

    @Test
    void deleteMeat() throws Exception{

        String meatId = "id";

        doNothing().when(meatServiceClient).deleteMeat(meatId);

        mockMvc.perform(delete("/api/v1/meats/" + meatId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(meatServiceClient, times(1)).deleteMeat(meatId);
    }
}