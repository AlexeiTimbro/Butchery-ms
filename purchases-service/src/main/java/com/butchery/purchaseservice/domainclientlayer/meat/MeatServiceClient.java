package com.butchery.purchaseservice.domainclientlayer.meat;


import com.butchery.purchaseservice.Utils.Exceptions.NotFoundException;
import com.butchery.purchaseservice.Utils.HttpErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class MeatServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String MEAT_SERVICE_BASE_URL;

    public MeatServiceClient(RestTemplate restTemplate,
                             ObjectMapper mapper,
                             @Value("${app.meats-service.host}") String meatServiceHost,
                             @Value("${app.meats-service.port}") String meatServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        MEAT_SERVICE_BASE_URL = "http://" + meatServiceHost +":" + meatServicePort + "/api/v1/meats";

    }

    //GET BY ID

    public MeatResponseModel getMeatByMeatId(String meatId){
        log.debug("3. Received in API-Gateway MeatServiceClient getMeatByMeatId with meatId: " + meatId);

        try{
            String url = MEAT_SERVICE_BASE_URL +"/" + meatId;

            MeatResponseModel meatResponseModel = restTemplate
                    .getForObject(url, MeatResponseModel.class);


            log.debug("5. Received in API-Gateway MeatServiceClient getMeatByMeatId with MeatResponseModel: "
                    + meatResponseModel.getMeatId());


            return meatResponseModel;
        }

        catch(HttpClientErrorException ex){
            log.debug("5. Received in API-Gateway MeatServiceClient getMeatByMeatId with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }


    public void updateMeatStatus(MeatRequestModel meatRequestModel,String meatId){

        log.debug("3. Received in API-Gateway Meat Service Client updateMeatStatus");

        try{
            String url=MEAT_SERVICE_BASE_URL+"/"+meatId;
            restTemplate.put(url,meatRequestModel);

            log.debug("5. Received in API-Gateway Meat Service Client updateMeatStatus with meatId: "
                    +meatId);

        }
        catch(HttpClientErrorException ex){
            log.debug("5. Received in API-Gateway Meat Service Client updateMeatStatus with exception: "
                    +ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }




    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch (IOException ioex) {
            return ioex.getMessage();
        }
    }


    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        //include all possible responses from the client

        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }


}
