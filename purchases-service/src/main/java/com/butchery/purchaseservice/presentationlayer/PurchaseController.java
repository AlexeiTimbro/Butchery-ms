package com.butchery.purchaseservice.presentationlayer;

import com.butchery.purchaseservice.businesslayer.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping()
    ResponseEntity <List<PurchaseResponseModel>> getAllPurchases(){
        return ResponseEntity.ok().body(purchaseService.getAllPurchase());
    }
}
