package com.pathumi.smartcampusapi.resources;

//JAX-RS annotations for defining REST endpoints
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

//used to define response format as JSON
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//collection utilities for storing sensor readings
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

//models
import com.pathumi.smartcampusapi.models.Sensor;
import com.pathumi.smartcampusapi.models.SensorReading;

//exceptions
import com.pathumi.smartcampusapi.exceptions.LinkedResourceNotFoundException;
import com.pathumi.smartcampusapi.exceptions.SensorUnavailableException;
import java.time.LocalDateTime;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    
    //stores readings per sensor
    public static Map<String, List<SensorReading>> readings = new HashMap<>();
    
    private String sensorId; //identifies which sensor this resource is for
    
    public SensorReadingResource(String sensorId){
        this.sensorId = sensorId;
    }
    
    //GET all readings for a specific sensor
    @GET
    public Response getReadings(){
        
        Sensor sensor = SensorResource.sensors.get(sensorId);
        
        if(sensor == null){
            throw new LinkedResourceNotFoundException("Sensor not found: " + sensorId);
        }
        
        List<SensorReading> sensorReadings = readings.getOrDefault(sensorId, new ArrayList<>());
        
        if(sensorReadings.isEmpty()){
            Map<String, String> message = new HashMap<>();
            message.put("message", "No readings found for this sensor.");
            return Response.ok(message).build();
        }
        return Response.ok(sensorReadings).build();
    }
    
    //POST new reading
    @POST
    public Response addReading(SensorReading reading){
        
        //to store messages and send as responses
        Map<String, String> error = new HashMap<>();
        
        //validate reading
        
        if(reading == null){
            error.put("error", "Reading value is required");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        
        if(reading.getId() == null || reading.getId().isEmpty()){
            error.put("error", "Reading ID is required");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        
        if(reading.getValue() <= 0){
            error.put("error", "Invalid reading value");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        
        Sensor sensor = SensorResource.sensors.get(sensorId);
        
        //ensure sensor exists
        if(sensor == null){
            throw new LinkedResourceNotFoundException("Sensor not found: " + sensorId);
        }
                
        //block if sensor is not available
        if("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())){
            throw new SensorUnavailableException("Sensor is under maintenance. Cannot add readings");
        }
        
        reading.setTimestamp(LocalDateTime.now());
        
        //initialize list if not present
        readings.putIfAbsent(sensorId, new ArrayList<>());
        
        //add new reading
        readings.get(sensorId).add(reading);
       
        //update latest sensor value
        sensor.setCurrentValue(reading.getValue());
        
        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
    
}
