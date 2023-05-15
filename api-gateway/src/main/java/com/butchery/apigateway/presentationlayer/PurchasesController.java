package com.butchery.apigateway.presentationlayer;


import com.butchery.apigateway.businesslayer.PurchaseService;
import com.butchery.apigateway.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/purchases")
public class PurchasesController{


    private final Integer UUID_SIZE = 36;


    private PurchaseService purchaseService;

    public PurchasesController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping(produces ="application/json")
    ResponseEntity<List<PurchaseResponseModel>> getAllPurchases(){

        //Keep track
        log.debug("1. Received in API-Gateway purchases Controller getAllPurchases.");

        return ResponseEntity.ok().body(purchaseService.getAllPurchasesAggregate());
    }

    @GetMapping(value = "/{purchaseId}", produces ="application/json")
    ResponseEntity<PurchaseResponseModel> getPurchaseByPurchaseId(@PathVariable String purchaseId){
        if(purchaseId.length() != UUID_SIZE){
            throw new NotFoundException("Purchase id is invalid: " + purchaseId);
        }

        //Keep track
        log.debug("1. Received in API-Gateway Meats Controller getMeatByMeatId with meatId: " + purchaseId);
        return ResponseEntity.ok().body(purchaseService.getPurchaseByPurchaseIdAggregate(purchaseId));
    }

    @DeleteMapping(value = "/{purchaseId}", produces ="application/json")
    ResponseEntity<Void> deletePurchase(@PathVariable String purchaseId){
        if(purchaseId.length() != UUID_SIZE){
            throw new NotFoundException("Purchase id is invalid: " + purchaseId);
        }

        //Keep track
        log.debug("1. Received in API-Gateway Purchase Controller deletePurchase with purchaseId: " + purchaseId);
        purchaseService.deletePurchaseAggregate(purchaseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}


