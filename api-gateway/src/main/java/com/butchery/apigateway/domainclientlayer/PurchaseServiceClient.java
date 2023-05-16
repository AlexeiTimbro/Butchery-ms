package com.butchery.apigateway.domainclientlayer;

import com.butchery.apigateway.presentationlayer.MeatRequestModel;
import com.butchery.apigateway.presentationlayer.MeatResponseModel;
import com.butchery.apigateway.presentationlayer.PurchaseRequestModel;
import com.butchery.apigateway.presentationlayer.PurchaseResponseModel;
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
public class PurchaseServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String PURCHASE_SERVICE_BASE_URL;
    private final String CUSTOMER_PURCHASE_SERVICE_BASE_URL;

    public PurchaseServiceClient(RestTemplate restTemplate,
                             ObjectMapper mapper,
                             @Value("${app.purchases-service.host}") String purchaseServiceHost,
                             @Value("${app.purchases-service.port}") String purchaseServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        PURCHASE_SERVICE_BASE_URL = "http://" + purchaseServiceHost +":" + purchaseServicePort + "/api/v1/purchases";

        CUSTOMER_PURCHASE_SERVICE_BASE_URL = "http://" + purchaseServiceHost +":" + purchaseServicePort + "/api/v1/customers";

    }

    //CRUD OPERATION
/*
    public List<PurchaseResponseModel> getAllPurchases(){

        try{
            String url = PURCHASE_SERVICE_BASE_URL;

            PurchaseResponseModel[] purchaseResponseArray = restTemplate
                    .getForObject(url, PurchaseResponseModel[].class);

            return Arrays.asList(purchaseResponseArray);
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

 */

    public List<PurchaseResponseModel> getAllCustomerPurchases(String customerId){

        try{
            String url = CUSTOMER_PURCHASE_SERVICE_BASE_URL + "/" + customerId + "/purchases";

            PurchaseResponseModel[] purchaseResponseArray = restTemplate
                    .getForObject(url, PurchaseResponseModel[].class);

            return Arrays.asList(purchaseResponseArray);
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }
/*
    public PurchaseResponseModel getPurchaseByPurchaseId(String purchaseId){

        try{
            String url = PURCHASE_SERVICE_BASE_URL +"/" + purchaseId;

            PurchaseResponseModel purchaseResponseModel = restTemplate
                    .getForObject(url, PurchaseResponseModel.class);

            return purchaseResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

 */

    public PurchaseResponseModel getPurchaseByCustomerIdAndPurchaseId(String customerId, String purchaseId){

        try{
            String url = CUSTOMER_PURCHASE_SERVICE_BASE_URL + "/" + customerId + "/purchases" + "/" + purchaseId;

            PurchaseResponseModel purchaseResponseModel = restTemplate
                    .getForObject(url, PurchaseResponseModel.class);

            return purchaseResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public PurchaseResponseModel addPurchase(PurchaseRequestModel purchaseRequestModel, String customerId){


        try{
            String url = CUSTOMER_PURCHASE_SERVICE_BASE_URL + "/" + customerId + "/purchases";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PurchaseRequestModel> requestEntity = new HttpEntity<>(purchaseRequestModel, headers);

            PurchaseResponseModel purchaseResponseModel =
                    restTemplate.postForObject(url, requestEntity, PurchaseResponseModel.class);

            return purchaseResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public PurchaseResponseModel updatePurchase(PurchaseRequestModel purchaseRequestModel, String customerId,String purchaseId){

        try{
            String url = CUSTOMER_PURCHASE_SERVICE_BASE_URL + "/" + customerId + "/purchases" + "/" + purchaseId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PurchaseRequestModel> requestEntity = new HttpEntity<>(purchaseRequestModel, headers);

            restTemplate.put(url, requestEntity,customerId, purchaseId);

            PurchaseResponseModel purchaseResponseModel = restTemplate
                    .getForObject(url, PurchaseResponseModel.class);

            return purchaseResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }
/*
    public void deletePurchase(String purchaseId) {

        try {
            String url = PURCHASE_SERVICE_BASE_URL + "/" + purchaseId;

            restTemplate.delete(url);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

 */

    public void deletePurchaseByCustomerIdAndPurchaseId(String customerId, String purchaseId) {

        try {
            String url = CUSTOMER_PURCHASE_SERVICE_BASE_URL + "/" + customerId + "/purchases" + "/" + purchaseId;

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
            return new InvalidInputException(ex);
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new PurchaseDateIsNotValid(getErrorMessage(ex));
        }
        return ex;
    }
}
