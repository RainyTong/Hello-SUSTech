package com.example.hellosustechbackend.service;

import com.example.hellosustechbackend.api.BuildingRepository;
import com.example.hellosustechbackend.domain.Building;
import com.example.hellosustechbackend.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This service will handle all building interactions
 */
@RestController
public class BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    /**
     * This method will save a building
     * @param building the building will be stored
     * @return {@code Status} with argument {@code true} if save successfully
     *          otherwise will return {@code false}
     */
    public Status saveBuilding(Building building) {
        Status status = new Status();
        buildingRepository.save(building);
        status.setResult(true);
        status.setDescription("Add building success");
        return status;
    }

    /**
     * This method will return the building with the name input
     * @param name the name of the building
     * @return {@code Building} whose name is the same as the input
     */
    public Building getBuilding(String name) {
        List<Building> buildingList = buildingRepository.findBuildingByName(name);
        if (buildingList.size() > 0) {
            return buildingList.get(0);
        } else {
            return null;
        }
    }

    /**
     * This method will return the all the buildings
     * @return {@code List<Building>} all the buildings
     */
    public List<Building> getAllBuilding() {
        return buildingRepository.findAll();
    }
}