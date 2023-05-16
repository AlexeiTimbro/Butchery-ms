package com.butchery.apigateway.presentationlayer;

import com.butchery.apigateway.businesslayer.CustomersService;
import com.butchery.apigateway.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
public class CustomersController {

    private final Integer UUID_SIZE = 36;

    private CustomersService customersService;

    public CustomersController(CustomersService customersService) {
        this.customersService = customersService;
    }

    @GetMapping(produces ="application/json")
    ResponseEntity<List<CustomerResponseModel>> getAllCustomers(){
        return ResponseEntity.ok().body(customersService.getAllCustomers());
    }

    @GetMapping(value = "/{customerId}", produces ="application/json")
    ResponseEntity<CustomerResponseModel> getCustomerByCustomerId(@PathVariable String customerId){
        return ResponseEntity.ok().body(customersService.getCustomerByCustomerId(customerId));
    }

    @PostMapping()
    ResponseEntity<CustomerResponseModel> addCustomer(@RequestBody CustomerRequestModel customerRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(customersService.addCustomer(customerRequestModel));
    }

    @PutMapping(value = "/{customerId}", produces ="application/json")
    ResponseEntity<CustomerResponseModel> updateCustomer(@RequestBody CustomerRequestModel customerRequestModel, @PathVariable String customerId){
        return ResponseEntity.ok().body(customersService.updateCustomer(customerRequestModel,customerId));
    }

    @DeleteMapping(value = "/{customerId}", produces ="application/json")
    ResponseEntity<Void> deleteMeat(@PathVariable String customerId){
        customersService.deleteCustomer(customerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
