package com.butchery.purchaseservice.presentationlayer;

import com.butchery.purchaseservice.businesslayer.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/customers/{customerId}/purchases")
@RequiredArgsConstructor
public class CustomerPurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping()
    ResponseEntity <List<PurchaseResponseModel>> getAllPurchases(){
        return ResponseEntity.ok().body(purchaseService.getAllPurchaseAggregate());
    }

    @PostMapping()
    ResponseEntity<PurchaseResponseModel> processCustomerPurchase(
            @RequestBody PurchaseRequestModel purchaseRequestModel,
            @PathVariable String customerId){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseService.processCustomerPurchase(purchaseRequestModel,customerId));
    }

    @PutMapping("/{purchaseId}")
    ResponseEntity<PurchaseResponseModel> updateCustomerPurchase(
            @RequestBody PurchaseRequestModel purchaseRequestModel,
            @PathVariable String customerId,
            @PathVariable String purchaseId){

        return ResponseEntity.ok().body(purchaseService.updateCustomerPurchase(purchaseRequestModel,customerId,purchaseId));
    }
}
