package com.butchery.apigateway.domainclientlayer;

import com.butchery.apigateway.presentationlayer.CustomerRequestModel;
import com.butchery.apigateway.presentationlayer.CustomerResponseModel;
import com.butchery.apigateway.presentationlayer.MeatRequestModel;
import com.butchery.apigateway.presentationlayer.MeatResponseModel;
import com.butchery.apigateway.utils.HttpErrorInfo;
import com.butchery.apigateway.utils.exceptions.*;
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

    //CRUD OPERATION

    public List<CustomerResponseModel> getAllCustomers(){
        try{
            String url = CUSTOMER_SERVICE_BASE_URL;

            CustomerResponseModel[] customerResponseArray = restTemplate
                    .getForObject(url, CustomerResponseModel[].class);

            return Arrays.asList(customerResponseArray);
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public CustomerResponseModel getCustomerByCustomerId(String customerId){

        try{
            String url = CUSTOMER_SERVICE_BASE_URL +"/" + customerId;

            CustomerResponseModel customerResponseModel = restTemplate
                    .getForObject(url, CustomerResponseModel.class);

            return customerResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel){

        try{
            String url = CUSTOMER_SERVICE_BASE_URL;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CustomerRequestModel> requestEntity = new HttpEntity<>(customerRequestModel, headers);

            CustomerResponseModel customerResponseModel =
                    restTemplate.postForObject(url, requestEntity, CustomerResponseModel.class);

            return customerResponseModel;
        }
        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }

    }

    public CustomerResponseModel updateCustomer(CustomerRequestModel customerRequestModel, String customerId){

        try{
            String url = CUSTOMER_SERVICE_BASE_URL +"/" + customerId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CustomerRequestModel> requestEntity = new HttpEntity<>(customerRequestModel, headers);

            restTemplate.put(url, requestEntity, customerId);

            CustomerResponseModel customerResponseModel = restTemplate
                    .getForObject(url, CustomerResponseModel.class);

            return customerResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public void deleteCustomer(String customerId) {

        try {
            String url = CUSTOMER_SERVICE_BASE_URL + "/" + customerId;

            restTemplate.delete(url);

        } catch (HttpClientErrorException ex) {
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
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new DuplicatePhoneNumberException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidEmailAddressException(getErrorMessage(ex));
        }
        return ex;
    }


}
