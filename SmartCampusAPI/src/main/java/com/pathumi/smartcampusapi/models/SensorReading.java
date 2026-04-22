package com.pathumi.smartcampusapi.models;


public class SensorReading {
    private String id;
    private long timestamp; //stores time of reading
    private double value; //actual sensor value
    
    public SensorReading() {} //required for frameworks like JSON mapping

    public SensorReading(String id, double value) {
        this.id = id;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    
    
    
}
