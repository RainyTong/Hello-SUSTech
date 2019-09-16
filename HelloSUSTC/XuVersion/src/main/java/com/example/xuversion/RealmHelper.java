package com.example.xuversion;


import com.example.xuversion.model.Building;
import com.example.xuversion.model.ClassInfo;
import com.example.xuversion.model.History;

import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmHelper {

    private Realm realm;

    /**
     * Constructor
     * @param realm the Realm object
     */
    public RealmHelper(Realm realm) {
        this.realm = realm;
    }

    /**
     * Add building
     * @param jsonArray Object of jsonArray
     */
    public void addBuildingByJSONArray(JSONArray jsonArray){
        realm.beginTransaction();
        realm.createOrUpdateAllFromJson(Building.class,jsonArray);
        realm.commitTransaction();
    }

    /**
     * add a class from json array
     * @param jsonArray the jsonarray
     */
    public void addClassByJSONArray(JSONArray jsonArray){
        realm.beginTransaction();
        realm.createOrUpdateAllFromJson(ClassInfo.class,jsonArray);
        realm.commitTransaction();
    }

    /**
     * quey by type
     * @param type the type name
     * @return the query result
     */
    public List<Building> queryByType(String type){
        RealmResults<Building> buildings = realm.where(Building.class)
                .equalTo("type", type).findAll();
        return realm.copyFromRealm(buildings);
    }

    /**
     * query similar building
     * @param s building name
     * @return the buildings
     */
    public List<Building> querySimilarBuilding(String s){
        RealmResults<Building> buildings = realm.where(Building.class)
                .contains("name", s)
                .findAll();
        return buildings;
    }

    /**
     * query all buildings
     * @return all buildings name
     */
    public List<Building> queryAllBuilding(){
        RealmResults<Building> buildings = realm.where(Building.class).findAll();
        return buildings;
    }

    /**
     * if contains a building
     * @param name the name of a building
     * @return if contains
     */
    public boolean iscontainBuilding(String name){
        RealmResults<Building> buildings = realm.where(Building.class)
                .equalTo("name", name)
                .findAll();
        return buildings.size()>0;
    }

    public Building getBuilding(String name){
        Building building = realm.where(Building.class)
                .equalTo("name", name)
                .findFirst();
        return building;
    }




    private boolean isContainHistory(String input){
        RealmResults<History> buildings = realm.where(History.class)
                .equalTo("history", input)
                .findAll();
        return buildings.size()>0;
    }

    /**
     * Add history
     * @param input the input
     */
    public void addHistory(String input){
        if(isContainHistory(input)){
            return;
        }
        // increatement index
        Number num = realm.where(History.class).max("id");
        long id = num == null ? 1 :(long) num+1;
        if(id > 10){
            id = id % 10;
        }
        History history = new History(id,input);
        realm.beginTransaction();
        realm.insertOrUpdate(history);
        realm.commitTransaction();
    }

    /**
     * query all history
     * @return the buildings
     */
    public List<Building> queryAllHistory() {
        RealmResults<History> histories = realm.where(History.class).findAllSorted("id", Sort.DESCENDING);
        List<Building> buildings = new ArrayList<>();
        for(int i=0;i < histories.size();i++){
            buildings.add(new Building(histories.get(i).getHistory(),"history"));
        }
        return buildings;
    }

    /**
     * delete all history
     */
    public void deleteAllHistory(){
        realm.beginTransaction();
        realm.where(History.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    /**
     * delete all class
     */
    public void deleteAllClass(){
        realm.beginTransaction();
        realm.where(ClassInfo.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    /**
     * delete all building
     */
    public void deleteAllBuilding(){
        realm.beginTransaction();
        realm.where(Building.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }







    /**
     * query class by day
     * @param weekday the week day that you want to query
     * @return the class information
     */
    public List<ClassInfo> queryClassByDay(int weekday) {
        RealmResults<ClassInfo> classInfos = realm.where(ClassInfo.class)
                .equalTo("weekday", weekday)
                .findAll();
        return classInfos;
    }

    /**
     * delete the class by day
     * @param classInfo the class information
     */
    public void deleteClassByDay(ClassInfo classInfo) {
        realm.beginTransaction();
        realm.where(ClassInfo.class)
                .equalTo("weekday", classInfo.getWeekday())
                .and()
                .equalTo("lecture",classInfo.getLecture()).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    /**
     * if a class contains
     * @param classInfo class information
     * @return true or false
     */
    public boolean isContainClass(ClassInfo classInfo){
        RealmResults<ClassInfo> buildings = realm.where(ClassInfo.class)
                .equalTo("weekday", classInfo.getWeekday())
                .and()
                .equalTo("lecture",classInfo.getLecture()).findAll();
        return buildings.size()>0;
    }

    /**
     * Class info getter
     * @param classInfo get class information
     * @return class information
     */
    public ClassInfo getClass(ClassInfo classInfo){
       ClassInfo first = realm.where(ClassInfo.class)
                .equalTo("weekday", classInfo.getWeekday())
                .and()
                .equalTo("lecture",classInfo.getLecture()).findFirst();
       return first;
    }

    /**
     * add class information
     * @param classInfo the class information
     */
    public void addClass(ClassInfo classInfo) {
        realm.beginTransaction();
        realm.insertOrUpdate(classInfo);
        realm.commitTransaction();
    }






}
