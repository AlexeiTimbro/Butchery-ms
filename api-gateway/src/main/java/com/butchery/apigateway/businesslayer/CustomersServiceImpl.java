package com.butchery.apigateway.businesslayer;

import com.butchery.apigateway.domainclientlayer.CustomerServiceClient;
import com.butchery.apigateway.presentationlayer.CustomerRequestModel;
import com.butchery.apigateway.presentationlayer.CustomerResponseModel;
import com.butchery.apigateway.presentationlayer.MeatRequestModel;
import com.butchery.apigateway.presentationlayer.MeatResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomersServiceImpl implements CustomersService {

    private CustomerServiceClient customerServiceClient;

    public CustomersServiceImpl(CustomerServiceClient customerServiceClient) {
        this.customerServiceClient = customerServiceClient;
    }

    @Override
    public List<CustomerResponseModel> getAllCustomers() {
        return customerServiceClient.getAllCustomers();
    }

    @Override
    public CustomerResponseModel getCustomerByCustomerId(String customerId) {
        return customerServiceClient.getCustomerByCustomerId(customerId);
    }

    @Override
    public CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel) {
        return customerServiceClient.addCustomer(customerRequestModel);
    }

    @Override
    public CustomerResponseModel updateCustomer(CustomerRequestModel customerRequestModel, String customerId) {
        return customerServiceClient.updateCustomer(customerRequestModel,customerId);
    }

    @Override
    public void deleteCustomer(String customerId) {
        customerServiceClient.deleteCustomer(customerId);
    }
}
