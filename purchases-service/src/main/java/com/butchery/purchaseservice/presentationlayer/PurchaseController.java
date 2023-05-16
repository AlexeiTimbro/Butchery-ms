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
@RequestMapping("api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping()
    public ResponseEntity <List<PurchaseResponseModel>> getAllPurchases(){
        return ResponseEntity.ok().body(purchaseService.getAllPurchaseAggregate());
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity <PurchaseResponseModel> getAllPurchaseById(@PathVariable String purchaseId){
        return ResponseEntity.ok().body(purchaseService.getPurchaseByPurchaseIdAggregate(purchaseId));
    }

    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<Void>removeButcher(@PathVariable String purchaseId) {
        purchaseService.deletePurchaseAggregate(purchaseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
