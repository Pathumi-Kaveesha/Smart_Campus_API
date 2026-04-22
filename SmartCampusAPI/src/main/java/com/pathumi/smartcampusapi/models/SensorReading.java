package com.pathumi.smartcampusapi.models;

import java.time.LocalDateTime;


public class SensorReading {
    private String id;
    private LocalDateTime timestamp; //stores time of reading
    private double value; //actual sensor value
    
    public SensorReading() {} //required for frameworks like JSON mapping

    public SensorReading(String id, double value) {
        this.id = id;
        this.value = value;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    
    
    
}
