package com.butchery.customerservice.businesslayer;


import com.butchery.customerservice.datalayer.Customer;
import com.butchery.customerservice.datalayer.CustomerRepository;
import com.butchery.customerservice.datamapperlayer.CustomerRequestMapper;
import com.butchery.customerservice.datamapperlayer.CustomerResponseMapper;
import com.butchery.customerservice.presentationlayer.CustomerRequestModel;
import com.butchery.customerservice.presentationlayer.CustomerResponseModel;
import com.butchery.customerservice.utils.exceptions.DuplicatePhoneNumberException;
import com.butchery.customerservice.utils.exceptions.InvalidEmailAddressException;
import com.butchery.customerservice.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.boot.context.properties.source.ConfigurationPropertyName.isValid;

@Service
public class CustomerServiceImpl implements CustomerService{

    private CustomerRepository customerRepository;
    private CustomerResponseMapper customerResponseMapper;
    private CustomerRequestMapper customerRequestMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerResponseMapper customerResponseMapper, CustomerRequestMapper customerRequestMapper) {
        this.customerRepository = customerRepository;
        this.customerResponseMapper = customerResponseMapper;
        this.customerRequestMapper = customerRequestMapper;
    }

    @Override
    public List<CustomerResponseModel> getCustomers() {
        return customerResponseMapper.entityListToResponseModelList(customerRepository.findAll());
    }

    @Override
    public CustomerResponseModel getCustomerByCustomerId(String customerId) {

        Customer customer = customerRepository.findByCustomerIdentifier_CustomerId(customerId);
        if (customer == null) {
            throw new NotFoundException("Unknown Customer id");
        }

        return customerResponseMapper.entityToResponseModel(customer);
    }

    @Override
    public CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel) {

        Customer customer = customerRequestMapper.requestModelToEntity(customerRequestModel);

        String phoneNumber = customerRequestModel.getPhoneNumber();
        List<Customer> customers = customerRepository.findCustomerByPhoneNumber(phoneNumber);
        if(customers.size() > 0){
            throw new DuplicatePhoneNumberException("This phone number is already in use by another customer.");
        }

        String email = customerRequestModel.getEmail();
        if (email != null && !email.isEmpty()) {
            if (!isValid(email)) {
                throw new InvalidEmailAddressException("This email: " + email + " is invalid.");
            }
        }

        return customerResponseMapper.entityToResponseModel(customerRepository.save(customer));
    }

    @Override
    public CustomerResponseModel updateCustomer(CustomerRequestModel customerRequestModel, String customerId) {

        Customer existingCustomer = customerRepository.findByCustomerIdentifier_CustomerId(customerId);
        if (existingCustomer == null) {
            throw new NotFoundException("Unknown Customer id");
        }

        Customer customer = customerRequestMapper.requestModelToEntity(customerRequestModel);
        customer.setId(existingCustomer.getId());
        customer.setCustomerIdentifier(existingCustomer.getCustomerIdentifier());

        return customerResponseMapper.entityToResponseModel(customerRepository.save(customer));
    }

    @Override
    public void removeCustomer(String customerId) {

        Customer existingCustomer = customerRepository.findByCustomerIdentifier_CustomerId(customerId);
        if (existingCustomer == null) {
            throw new NotFoundException("Unknown Customer id");
        }

        customerRepository.delete(existingCustomer);
    }


    private boolean isValid(String emailAddress) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(emailAddress).matches();
    }


}
