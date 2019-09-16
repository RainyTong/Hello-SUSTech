package com.example.xuversion.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Building extends RealmObject {
    private String name;
    private String type;
    private String description;
    private double longitude;
    private double latitude;
    private int buildingLevel;

    @PrimaryKey
    private long id;

    /**
     * Constructor
     */
    public Building(){

    }

    /**
     * Constructor with parameters
     * @param name the name of the building
     * @param type the type of the building
     */
    public Building(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Name getter
     * @return the name of the building
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter
     * @param name the name that you want to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Type getter
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Type setter
     * @param type the type you want to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Description getter
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * description setter
     * @param description the description you want to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * longitude getter
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * latitude setter
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * the id getter
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * the id setter
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }
    
}
