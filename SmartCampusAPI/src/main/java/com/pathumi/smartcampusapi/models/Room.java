package com.pathumi.smartcampusapi.models;
import java.util.ArrayList;
import java.util.List;


public class Room {
    private String id;
    private String name;
    private int capacity;
    private List<String> sensorIds = new ArrayList<>(); //stores IDs of sensors
    
    public Room(){} //required for frameworks like JSON mapping
    
    public Room(String id, String name, int capacity){
        this.id = id;
        this.name = name;
        setCapacity(capacity); //ensures validation is applied
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity){
        if(capacity < 0){
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        this.capacity = capacity;
    }
    
    public List<String> getSensorIds(){
        return sensorIds;
    } 

    public void setSensorIds(List<String> sensorIds) {
        if(sensorIds == null){
            this.sensorIds = new ArrayList<>(); //avoid null list issues
        }else{
            this.sensorIds = sensorIds;
        }
    }

         
}
