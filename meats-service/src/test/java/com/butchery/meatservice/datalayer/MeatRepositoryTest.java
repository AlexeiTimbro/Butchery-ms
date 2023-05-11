package com.butchery.meatservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MeatRepositoryTest {

    private Meat preSavedMeat;

    @Autowired
    MeatRepository meatRepository;

    @BeforeEach
    public void setup(){
        meatRepository.deleteAll();
        preSavedMeat = meatRepository.save(new Meat("Duck",Status.AVAILABLE, "Wild", "Super Tender", "15-05-2026", 20.80));
    }
    @Test
    public void saveNewMeat_shouldSucceed(){

        // arrange
        String expectedAnimal = "Duck";

        String expectedEnvironment = "Wild";
        String expectedTexture = "Super Tender";
        String expectedExpirationDate = "15-05-2026";
        Double expectedPrice = 20.34;

        Meat newMeat= new Meat(expectedAnimal,Status.AVAILABLE, expectedEnvironment,expectedTexture,expectedExpirationDate,expectedPrice);

        // act
        Meat savedMeat= meatRepository.save(newMeat);

        //assert
        assertNotNull(savedMeat);
        assertNotNull(savedMeat.getId());
        assertNotNull(savedMeat.getMeatIdentifier().getMeatId());
        assertEquals(expectedAnimal, savedMeat.getAnimal());
        assertEquals(expectedEnvironment, savedMeat.getEnvironment());
        assertEquals(expectedTexture, savedMeat.getTexture());
        assertEquals(expectedExpirationDate, savedMeat.getExpirationDate());
    }

    @Test
    public void updateMeat_shouldSucceed(){
        // arrange
        String updatedMeat = "meats";
        preSavedMeat.setAnimal("Duck");

        //act
        Meat savedMeat= meatRepository.save(preSavedMeat);

        // assert
        assertNotNull(savedMeat);
        assertThat(savedMeat, samePropertyValuesAs(preSavedMeat));

    }

    @Test
    public void findByMeatIdentifier_MetaId_ShouldSucceed(){
        // act

        Meat found = meatRepository.findMeatByMeatIdentifier_MeatId(
                preSavedMeat.getMeatIdentifier().getMeatId());

        // assert
        assertNotNull(found);
        assertThat(preSavedMeat, samePropertyValuesAs(found));
    }

    @Test
    public void findByInvalidMeatIdentifier_MeatId_ShouldReturnNull(){
        Meat found = meatRepository.findMeatByMeatIdentifier_MeatId(
                preSavedMeat.getMeatIdentifier().getMeatId() + 1);
        // assert
        assertNull(found);
    }
}