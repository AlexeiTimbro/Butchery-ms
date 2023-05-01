package com.butchery.butcherservice.datalayer;

import com.butchery.butcherservice.utils.HttpErrorInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import  static  org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ButcherPersistence {


    private Butcher preSavedButcher;
    @Autowired
    ButcherRepository butcherRepository;

    @BeforeEach
    public void setup(){
        butcherRepository.deleteAll();
        preSavedButcher = butcherRepository.save(new Butcher("Micheal","Jordan",20,"micheal.jordan@gmail.com","202-721-9507",34670.00,4.2,"Hilton","Paris","Quebec","Romania","5R6 28T"));
    }
    @Test
    public void saveNewButcher_shouldSucceed(){

        // arrange
        String expectedFirstName = "Micheal";
        String expectedLastName = "Jordan";
        Integer expectedAge = 20;
        String expectedEmail = "micheal.jordan@gmail.com";
        String expectedPhoneNumber = "202-721-9507";
        Double expectedSalary = 34670.00;
        Double expectedCommissionRate = 4.2;
        String expectedStreet = "Hilton";
        String expectedCity = "Paris";
        String expectedProvince = "Quebec";
        String expectedCountry = "Romania";
        String expectedPostalCode = "5R6 28T";

        Butcher newButcher= new Butcher(expectedFirstName,expectedLastName,expectedAge,expectedEmail,expectedPhoneNumber,expectedSalary,expectedCommissionRate,expectedStreet,expectedCity,expectedProvince,expectedCountry,expectedPostalCode);


        // act
        Butcher savedButcher= butcherRepository.save(newButcher);

        //assert
        assertNotNull(savedButcher);
        assertNotNull(savedButcher.getId());
        assertNotNull(savedButcher.getButcherIdentifier().getButcherId());
        assertEquals(expectedFirstName, savedButcher.getFirstName());
        assertEquals(expectedLastName, savedButcher.getLastName());
        assertEquals(expectedAge, savedButcher.getAge());
        assertEquals(expectedEmail, savedButcher.getEmail());
        assertEquals(expectedPhoneNumber, savedButcher.getPhoneNumber());
        assertEquals(expectedSalary, savedButcher.getSalary());
        assertEquals(expectedCommissionRate, savedButcher.getCommissionRate());
        assertEquals(expectedStreet, savedButcher.getStreet());
        assertEquals(expectedCity, savedButcher.getCity());
        assertEquals(expectedProvince, savedButcher.getProvince());
        assertEquals(expectedCountry, savedButcher.getCountry());
        assertEquals(expectedPostalCode, savedButcher.getPostalCode());


    }

    @Test
    public void updateButcher_shouldSucceed(){
        // arrange
        String updatedButcher = "butchers";
        preSavedButcher.setFirstName(updatedButcher);

        //act
        Butcher savedButcher= butcherRepository.save(preSavedButcher);

        // assert
        assertNotNull(savedButcher);
        assertThat(savedButcher, samePropertyValuesAs(preSavedButcher));

    }

    @Test
    public void findByButcherIdentifier_ButcherId_ShouldSucceed(){
        // act

        Butcher found = butcherRepository.findButcherByButcherIdentifier_ButcherId(
                preSavedButcher.getButcherIdentifier().getButcherId());

        // assert
        assertNotNull(found);
        assertThat(preSavedButcher, samePropertyValuesAs(found));
    }

    @Test
    public void findByInvalidButcherIdentifier_ButcherId_ShouldReturnNull(){
        Butcher found = butcherRepository.findButcherByButcherIdentifier_ButcherId(
                preSavedButcher.getButcherIdentifier().getButcherId() + 1);

        // assert
        assertNull(found);

    }

    @Test
    public void existsButcherIdentifier_ButcherId_ShouldReturnTrue(){

        // act
        boolean found = butcherRepository
                .existsByButcherIdentifier_ButcherId(
                        preSavedButcher.getButcherIdentifier().getButcherId());

        // assert
        assertTrue(found);

    }

    @Test
    public void existsButcherIdentifier_ButcherId_ShouldReturnFalse(){

        // act
        boolean found = butcherRepository
                .existsByButcherIdentifier_ButcherId(
                        preSavedButcher.getButcherIdentifier().getButcherId() + 1);


        // assert
        assertFalse(found);

    }


    @Test
    public void findButcherByPhoneNumber_Succeed(){

        List<Butcher> found = butcherRepository.findButcherByPhoneNumber(preSavedButcher.getPhoneNumber());

        assertEquals(1,found.size());
        assertEquals(preSavedButcher.getFirstName(), found.get(0).getFirstName());
        assertEquals(preSavedButcher.getLastName(), found.get(0).getLastName());
        assertEquals(preSavedButcher.getAge(), found.get(0).getAge());
        assertEquals(preSavedButcher.getEmail(), found.get(0).getEmail());
        assertEquals(preSavedButcher.getPhoneNumber(), found.get(0).getPhoneNumber());
        assertEquals(preSavedButcher.getSalary(), found.get(0).getSalary());
        assertEquals(preSavedButcher.getCommissionRate(), found.get(0).getCommissionRate());
        assertEquals(preSavedButcher.getStreet(), found.get(0).getStreet());
        assertEquals(preSavedButcher.getCity(), found.get(0).getCity());
        assertEquals(preSavedButcher.getProvince(), found.get(0).getProvince());
        assertEquals(preSavedButcher.getCountry(), found.get(0).getCountry());
        assertEquals(preSavedButcher.getPostalCode(), found.get(0).getPostalCode());
    }

    @Test
    public void findButcherByPhoneNumber_Failed(){

        List<Butcher> found = butcherRepository.findButcherByPhoneNumber(preSavedButcher.getPhoneNumber() + "1234");

        //assert
        assertEquals(0,found.size());

    }




}
