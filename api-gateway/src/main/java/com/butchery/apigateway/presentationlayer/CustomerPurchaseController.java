package com.butchery.apigateway.presentationlayer;

import com.butchery.apigateway.businesslayer.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/customers/{customerId}/purchases")
public class CustomerPurchaseController {

    private final Integer UUID_SIZE = 36;

    private PurchaseService purchaseService;

    public CustomerPurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping(produces ="application/json")
    ResponseEntity<List<PurchaseResponseModel>> getAllCustomerPurchases(@PathVariable String customerId){
        return ResponseEntity.ok().body(purchaseService.getAllCustomerPurchases(customerId));
    }

    @GetMapping(value ="/{purchaseId}" ,produces ="application/json")
    ResponseEntity<PurchaseResponseModel> getPurchaseByCustomerIdAndPurchaseId(@PathVariable String customerId, @PathVariable String purchaseId){
        return ResponseEntity.ok().body(purchaseService.getCustomerPurchaseByCustomerAndPurchaseId(customerId,purchaseId));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    ResponseEntity<PurchaseResponseModel> processCustomerPurchase(@RequestBody PurchaseRequestModel purchaseRequestModel,
                                                           @PathVariable String customerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseService.processCustomerPurchase(purchaseRequestModel,customerId));
    }

    @PutMapping(value = "/{purchaseId}", consumes = "application/json", produces = "application/json")
    ResponseEntity<PurchaseResponseModel> updateCustomerPurchase(@RequestBody PurchaseRequestModel purchaseRequestModel, @PathVariable String customerId, @PathVariable String purchaseId) {
        return ResponseEntity.ok().body(purchaseService.updateCustomerPurchase(purchaseRequestModel,customerId,purchaseId));
    }

    @DeleteMapping(value = "/{purchaseId}", produces = "application/json")
    ResponseEntity<Void> deletePurchaseByCustomerIdAndPurchaseId(@PathVariable String customerId, @PathVariable String purchaseId) {
        purchaseService.deleteCustomerPurchaseByCustomerAndPurchaseId(customerId,purchaseId);
        return ResponseEntity.noContent().build();
    }

}
