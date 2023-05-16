package com.butchery.apigateway.domainclientlayer;

import com.butchery.apigateway.businesslayer.ButchersService;
import com.butchery.apigateway.presentationlayer.*;
import com.butchery.apigateway.utils.HttpErrorInfo;
import com.butchery.apigateway.utils.exceptions.*;
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
public class ButcherServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String BUTCHER_SERVICE_BASE_URL;

    public ButcherServiceClient(RestTemplate restTemplate,
                             ObjectMapper mapper,
                             @Value("${app.butchers-service.host}") String butcherServiceHost,
                             @Value("${app.butchers-service.port}") String butcherServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        BUTCHER_SERVICE_BASE_URL = "http://" + butcherServiceHost +":" + butcherServicePort + "/api/v1/butchers";

    }

    //CRUD OPERATION

    public List<ButcherResponseModel> getAllButchers(){

        try{
            String url = BUTCHER_SERVICE_BASE_URL;

            ButcherResponseModel[] butcherResponseArray = restTemplate
                    .getForObject(url, ButcherResponseModel[].class);

            return Arrays.asList(butcherResponseArray);
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public ButcherResponseModel getButcherByButcherId(String butcherId){

        try{
            String url = BUTCHER_SERVICE_BASE_URL +"/" + butcherId;

            ButcherResponseModel butcherResponseModel = restTemplate
                    .getForObject(url, ButcherResponseModel.class);

            return butcherResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public ButcherResponseModel addButcher(ButcherRequestModel butcherRequestModel){


        try{
            String url = BUTCHER_SERVICE_BASE_URL;

            ButcherResponseModel butcherResponseModel =
                    restTemplate.postForObject(url, butcherRequestModel, ButcherResponseModel.class);

            return butcherResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public ButcherResponseModel updateButcher(ButcherRequestModel butcherRequestModel, String butcherId){

        try{
            String url = BUTCHER_SERVICE_BASE_URL +"/" + butcherId;

            restTemplate.put(url, butcherRequestModel, butcherId);

            ButcherResponseModel butcherResponseModel = restTemplate
                    .getForObject(url, ButcherResponseModel.class);

            return butcherResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

    public void deleteButcher(String butcherId) {

        try {
            String url = BUTCHER_SERVICE_BASE_URL + "/" + butcherId;

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

    public RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        //include all possible responses from the client

        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }

        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new DuplicatePhoneNumberException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new ButcherIsTooYoungException(getErrorMessage(ex));
        }
        return ex;
    }
}
