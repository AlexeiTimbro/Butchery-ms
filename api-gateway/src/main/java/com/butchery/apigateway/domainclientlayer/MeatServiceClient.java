package com.butchery.apigateway.domainclientlayer;

import com.butchery.apigateway.presentationlayer.CustomerRequestModel;
import com.butchery.apigateway.presentationlayer.CustomerResponseModel;
import com.butchery.apigateway.presentationlayer.MeatRequestModel;
import com.butchery.apigateway.presentationlayer.MeatResponseModel;
import com.butchery.apigateway.utils.HttpErrorInfo;
import com.butchery.apigateway.utils.exceptions.NotFoundException;
import com.butchery.apigateway.utils.exceptions.PriceLessOrEqualToZeroException;
import com.butchery.apigateway.utils.exceptions.ThisFieldIsRequiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RequestCallback;
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

    //CRUD OPERATION

    public List<MeatResponseModel> getAllMeats(){

        try{
            String url = MEAT_SERVICE_BASE_URL;

            MeatResponseModel[] meatResponseArray = restTemplate
                    .getForObject(url, MeatResponseModel[].class);

            return Arrays.asList(meatResponseArray);
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public MeatResponseModel getMeatByMeatId(String meatId){

        try{
            String url = MEAT_SERVICE_BASE_URL +"/" + meatId;

            MeatResponseModel meatResponseModel = restTemplate
                    .getForObject(url, MeatResponseModel.class);

            return meatResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public MeatResponseModel addMeat(MeatRequestModel meatRequestModel){

        try{
            String url = MEAT_SERVICE_BASE_URL;

            MeatResponseModel meatResponseModel =
                    restTemplate.postForObject(url, meatRequestModel, MeatResponseModel.class);

            return meatResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public MeatResponseModel updateMeat(MeatRequestModel meatRequestModel, String meatId){

        try{
            String url = MEAT_SERVICE_BASE_URL +"/" + meatId;

            restTemplate.execute(url, HttpMethod.PUT, requestCallback(meatRequestModel), clientHttpResponse -> null);

            MeatResponseModel meatResponseModel = restTemplate
                    .getForObject(url, MeatResponseModel.class);

            return meatResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public void deleteMeat(String meatId) {

        try {
            String url = MEAT_SERVICE_BASE_URL + "/" + meatId;

            restTemplate.execute(url, HttpMethod.DELETE, null, null);

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
        /*
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new PriceLessOrEqualToZeroException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new ThisFieldIsRequiredException(getErrorMessage(ex));
        }

         */
        return ex;
    }


    private RequestCallback requestCallback(final MeatRequestModel meatRequestModel) {
        return clientHttpRequest -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(clientHttpRequest.getBody(), meatRequestModel);
            clientHttpRequest.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            clientHttpRequest.getHeaders().add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        };
    }


}
