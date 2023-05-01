package com.butchery.apigateway.businesslayer;

import com.butchery.apigateway.presentationlayer.CustomerRequestModel;
import com.butchery.apigateway.presentationlayer.CustomerResponseModel;
import com.butchery.apigateway.presentationlayer.MeatRequestModel;
import com.butchery.apigateway.presentationlayer.MeatResponseModel;

import java.util.List;

public interface CustomersService {

    List<CustomerResponseModel> getAllCustomers();

    CustomerResponseModel getCustomerByCustomerId(String customerId);

    CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel);

    CustomerResponseModel updateCustomer(CustomerRequestModel customerRequestModel, String customerId);

    void deleteCustomer(String customerId);
}
