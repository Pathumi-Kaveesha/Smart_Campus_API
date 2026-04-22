package com.pathumi.smartcampusapi.models;

public class Sensor {
    private String id;
    private String type;
    private String status;
    private double currentValue;
    private String roomId;
    
    public Sensor(){}
    
    public Sensor(String id, String type, String status, double currentValue, String roomId){
        //using setters ensures validation rules are applied
        setId(id);
        setType(type);
        setStatus(status);
        this.currentValue = 0.0;
        setRoomId(roomId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(id == null || id.isEmpty()){
            throw new IllegalArgumentException("Sensor ID is required"); //prevents invalid IDs
        }
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if(type == null || type.isEmpty()){
            throw new IllegalArgumentException("Sensor type is required"); //ensures meaningful type
        }
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if(status == null){
            throw new IllegalArgumentException("Status is required");
        }
        String upper = status.toUpperCase(); //normalize input
        
        //only allow predefined states
        if(!upper.equals("ACTIVE") && !upper.equals("MAINTENANCE") && !upper.equals("OFFLINE")){
            throw new IllegalArgumentException("Status must be ACTIVE, MAINTENANCE or OFFLINE");
        }
        
        this.status = upper;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        if(roomId == null || roomId.isEmpty()){
            throw new IllegalArgumentException("Room ID is required"); //ensures sensor is linked to a room
        }
        this.roomId = roomId;
    }
    
    
}
