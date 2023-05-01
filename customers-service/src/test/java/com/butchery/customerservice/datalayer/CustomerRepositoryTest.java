package com.butchery.customerservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    private Customer preSavedCustomer;

    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    public void setup(){
        customerRepository.deleteAll();
        preSavedCustomer = customerRepository.save(new Customer("Micheal","Jordan","micheal.jordan@gmail.com","202-721-9507","Hilton","Paris","Quebec","Romania","5R6 28T"));
    }
    @Test
    public void saveNewCustomer_shouldSucceed(){

        // arrange
        String expectedFirstName = "Micheal";
        String expectedLastName = "Jordan";
        String expectedEmail = "micheal.jordan@gmail.com";
        String expectedPhoneNumber = "202-721-9507";
        String expectedStreet = "Hilton";
        String expectedCity = "Paris";
        String expectedProvince = "Quebec";
        String expectedCountry = "Romania";
        String expectedPostalCode = "5R6 28T";

        Customer newCustomer= new Customer(expectedFirstName,expectedLastName,expectedEmail,expectedPhoneNumber,expectedStreet,expectedCity,expectedProvince,expectedCountry,expectedPostalCode);

        // act
        Customer savedCustomer= customerRepository.save(newCustomer);

        //assert
        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getId());
        assertNotNull(savedCustomer.getCustomerIdentifier().getCustomerId());
        assertEquals(expectedFirstName, savedCustomer.getFirstName());
        assertEquals(expectedLastName, savedCustomer.getLastName());
        assertEquals(expectedEmail, savedCustomer.getEmail());
        assertEquals(expectedPhoneNumber, savedCustomer.getPhoneNumber());
        assertEquals(expectedStreet, savedCustomer.getStreet());
        assertEquals(expectedCity, savedCustomer.getCity());
        assertEquals(expectedProvince, savedCustomer.getProvince());
        assertEquals(expectedCountry, savedCustomer.getCountry());
        assertEquals(expectedPostalCode, savedCustomer.getPostalCode());

    }

    @Test
    public void updateCustomer_shouldSucceed(){
        // arrange
        String updatedCustomer = "customers";
        preSavedCustomer.setFirstName(updatedCustomer);

        //act
        Customer savedCustomer= customerRepository.save(preSavedCustomer);

        // assert
        assertNotNull(savedCustomer);
        assertThat(savedCustomer, samePropertyValuesAs(preSavedCustomer));

    }

    @Test
    public void findByCustomerIdentifier_CustomerId_ShouldSucceed(){
        // act

        Customer found = customerRepository.findByCustomerIdentifier_CustomerId(
                preSavedCustomer.getCustomerIdentifier().getCustomerId());

        // assert
        assertNotNull(found);
        assertThat(preSavedCustomer, samePropertyValuesAs(found));
    }

    @Test
    public void findByInvalidCustomerIdentifier_CustomerId_ShouldReturnNull(){
        Customer found = customerRepository.findByCustomerIdentifier_CustomerId(
                preSavedCustomer.getCustomerIdentifier().getCustomerId() + 1);
        // assert
        assertNull(found);

    }

    @Test
    public void findCustomerByPhoneNumber_Succeed(){

        List<Customer> found = customerRepository.findCustomerByPhoneNumber(preSavedCustomer.getPhoneNumber());

        assertEquals(1,found.size());
        assertEquals(preSavedCustomer.getFirstName(), found.get(0).getFirstName());
        assertEquals(preSavedCustomer.getLastName(), found.get(0).getLastName());
        assertEquals(preSavedCustomer.getEmail(), found.get(0).getEmail());
        assertEquals(preSavedCustomer.getPhoneNumber(), found.get(0).getPhoneNumber());
        assertEquals(preSavedCustomer.getStreet(), found.get(0).getStreet());
        assertEquals(preSavedCustomer.getCity(), found.get(0).getCity());
        assertEquals(preSavedCustomer.getProvince(), found.get(0).getProvince());
        assertEquals(preSavedCustomer.getCountry(), found.get(0).getCountry());
        assertEquals(preSavedCustomer.getPostalCode(), found.get(0).getPostalCode());
    }

    @Test
    public void findCustomerByPhoneNumber_Failed(){

        List<Customer> found = customerRepository.findCustomerByPhoneNumber(preSavedCustomer.getPhoneNumber()+ "1234");

        //assert
        assertEquals(0,found.size());

    }


}