package com.butchery.meatservice.datalayer;

import jakarta.persistence.Embeddable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Embeddable
public interface MeatRepository extends JpaRepository<Meat, Integer> {

    Meat findMeatByMeatIdentifier_MeatId(String meatId);
}
