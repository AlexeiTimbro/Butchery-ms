package com.butchery.customerservice.businesslayer;


import com.butchery.customerservice.presentationlayer.CustomerRequestModel;
import com.butchery.customerservice.presentationlayer.CustomerResponseModel;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseModel> getCustomers();

    CustomerResponseModel getCustomerByCustomerId(String customerId);

    CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel);

    CustomerResponseModel updateCustomer(CustomerRequestModel customerRequestModel, String customerId);

    void removeCustomer(String customerId);
}
