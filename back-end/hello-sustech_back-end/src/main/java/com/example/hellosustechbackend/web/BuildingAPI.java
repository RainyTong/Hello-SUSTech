package com.example.hellosustechbackend.web;


import com.example.hellosustechbackend.domain.Building;
import com.example.hellosustechbackend.service.BuildingService;
import com.example.hellosustechbackend.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The building api
 */
@RestController
@RequestMapping("/building")
public class BuildingAPI {

    @Autowired
    private BuildingService buildingService;

    /**
     * Add a building
     * @param building the building will be stored
     * @return {@code Status} with argument {@code true} if save successfully
     *          otherwise will return {@code false}
     */
    @PostMapping("/add")
    public Status saveBuilding(Building building) {
        return buildingService.saveBuilding(building);
    }

    /**
     * This method will return the building by the name
     * @param name the name of the building
     * @return {@code Building} whose name is the same as the input
     */
    @GetMapping("/get")
    public Building getBuilding(@RequestHeader("name") String name) {
        return buildingService.getBuilding(name);
    }

    /**
     * This method will return the all the buildings
     * @return {@code List<Building>} all the buildings
     */
    @GetMapping("/getAll")
    public List<Building> getAllBuilding() {
        return buildingService.getAllBuilding();
    }
}