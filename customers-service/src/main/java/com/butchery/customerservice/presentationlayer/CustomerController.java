package com.butchery.customerservice.presentationlayer;

import com.butchery.customerservice.businesslayer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponseModel>> getCustomers() {
        return ResponseEntity.ok().body(customerService.getCustomers());
    }


    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseModel> getCustomerByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok().body(customerService.getCustomerByCustomerId(customerId));
    }

    @PostMapping()
    public ResponseEntity<CustomerResponseModel> addCustomer(@RequestBody CustomerRequestModel customerRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addCustomer(customerRequestModel));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseModel> updateCustomer(@RequestBody CustomerRequestModel customerRequestModel, @PathVariable String customerId) {
        return ResponseEntity.ok().body(customerService.updateCustomer(customerRequestModel, customerId));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity <Void> removeCustomer(@PathVariable String customerId) {
        customerService.removeCustomer(customerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
