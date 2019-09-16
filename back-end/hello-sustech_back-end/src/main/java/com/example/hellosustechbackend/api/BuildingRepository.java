package com.example.hellosustechbackend.api;

import com.example.hellosustechbackend.domain.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Define the interface interact with the database
 */
public interface BuildingRepository extends JpaRepository<Building, Long> {
    /**
     * Get {@code Building} whose name is the input
     * @param name the name of the building
     * @return {@code Building} whose name is the input
     */
    List<Building> findBuildingByName(String name);
}