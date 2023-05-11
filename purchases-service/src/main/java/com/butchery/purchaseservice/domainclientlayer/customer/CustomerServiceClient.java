package com.butchery.purchaseservice.domainclientlayer.customer;

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
public class CustomerServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String CUSTOMER_SERVICE_BASE_URL;

    public CustomerServiceClient(RestTemplate restTemplate,
                                 ObjectMapper mapper,
                                 @Value("${app.customers-service.host}") String customerServiceHost,
                                 @Value("${app.customers-service.port}") String customerServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        CUSTOMER_SERVICE_BASE_URL = "http://" + customerServiceHost +":" + customerServicePort + "/api/v1/customers";

    }

    //GET BY ID

    public CustomerResponseModel getCustomerByCustomerId(String customerId){
        log.debug("3. Received in API-Gateway CustomerServiceClient getCustomerByCustomerId with customerId: " + customerId);

        try{
            String url = CUSTOMER_SERVICE_BASE_URL +"/" + customerId;

            CustomerResponseModel customerResponseModel = restTemplate
                    .getForObject(url, CustomerResponseModel.class);


            log.debug("5. Received in API-Gateway CustomerServiceClient getCustomerByCustomerId with CustomerResponseModel: "
                    + customerResponseModel.getCustomerId());

            return customerResponseModel;
        }

        catch(HttpClientErrorException ex){
            log.debug("5. Received in API-Gateway CustomerServiceClient getCustomerByCustomerId with exception: " + ex.getMessage());
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
