package com.butchery.apigateway.domainclientlayer;

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

    //INVENTORY CRUD OPERATION

    public List<MeatResponseModel> getAllMeats(){
        log.debug("3. Received in API-Gateway MeatServiceClient getAllMeats.");

        try{
            String url = MEAT_SERVICE_BASE_URL;

            MeatResponseModel[] meatResponseArray = restTemplate
                    .getForObject(url, MeatResponseModel[].class);

            return Arrays.asList(meatResponseArray);
        }

        catch(HttpClientErrorException ex){
            log.debug("5. Received in API-Gateway MeatServiceClient getAllMeats with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

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

    public MeatResponseModel addMeat(MeatRequestModel meatRequestModel){

        log.debug("3. Received in API-Gateway MeatServiceClient addMeat.");

        try{
            String url = MEAT_SERVICE_BASE_URL;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<MeatRequestModel> requestEntity = new HttpEntity<>(meatRequestModel, headers);

            MeatResponseModel meatResponseModel =
                    restTemplate.postForObject(url, requestEntity, MeatResponseModel.class);

            return meatResponseModel;
        }

        catch(HttpClientErrorException ex){
            log.debug("5. Received in API-Gateway MeatServiceClient addMeat with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

    public MeatResponseModel updateMeat(MeatRequestModel meatRequestModel, String meatId){
        log.debug("3. Received in API-Gateway MeatServiceClient updateMeat with meatId: " + meatId);

        try{
            String url = MEAT_SERVICE_BASE_URL +"/" + meatId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<MeatRequestModel> requestEntity = new HttpEntity<>(meatRequestModel, headers);

            restTemplate.put(url, requestEntity, meatId);

            MeatResponseModel meatResponseModel = restTemplate
                    .getForObject(url, MeatResponseModel.class);


            log.debug("5. Received in API-Gateway MeatServiceClient updateMeat with MeatResponseModel: "
                    + meatResponseModel.getMeatId());

            return meatResponseModel;
        }

        catch(HttpClientErrorException ex){
            log.debug("5. Received in API-Gateway MeatServiceClient updateMeat with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

    public void deleteMeat(String meatId) {
        log.debug("3. Received in API-Gateway MeatServiceClient deleteMeat with meatId: " + meatId);

        try {
            String url = MEAT_SERVICE_BASE_URL + "/" + meatId;

            restTemplate.delete(url);

            log.debug("5. Successfully deleted meat with id: " + meatId);
        } catch (HttpClientErrorException ex) {
            log.debug("5. Received in API-Gateway MeatServiceClient deleteMeat with exception: " + ex.getMessage());
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
            return new PriceLessOrEqualToZeroException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new ThisFieldIsRequiredException(getErrorMessage(ex));
        }
        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }


}
