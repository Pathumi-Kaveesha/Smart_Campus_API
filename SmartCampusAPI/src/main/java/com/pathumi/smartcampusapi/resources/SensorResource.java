package com.pathumi.smartcampusapi.resources;

//JAX-RS annotations for defining REST endpoints
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

//used to define response format as JSON
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//collection utilities for storing and filtering sensors
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

//models
import com.pathumi.smartcampusapi.models.Room;
import com.pathumi.smartcampusapi.models.Sensor;

//exceptions
import com.pathumi.smartcampusapi.exceptions.LinkedResourceNotFoundException;


@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {
    public static Map<String, Sensor> sensors = new HashMap<>();
    
    //GET all sensors
    @GET
    public Response getSensors(@QueryParam("type") String type){
        if(type == null){
            if(sensors.isEmpty()){
                throw new LinkedResourceNotFoundException("No sensors available");
            }
            return Response.ok(sensors.values()).build(); //return all if no filter
        }
        
        List<Sensor> filtered = new ArrayList<>();
        
        //filter sensors by type
        for(Sensor s: sensors.values()){
            if(s.getType() != null && s.getType().equalsIgnoreCase(type)){
                filtered.add(s);
            }
        }
        
        if(filtered.isEmpty()){
            throw new LinkedResourceNotFoundException("No sensors found for type: " + type);
        }
        return Response.ok(filtered).build();
    }

    
    //POST create sensor
    @POST
    public Response createSensor(Sensor sensor){
        
        Map<String, String> error = new HashMap<>();
        
        //basic validation before saving
        if(sensor == null || sensor.getId() == null || sensor.getId().isEmpty() || 
                sensor.getRoomId()== null || sensor.getRoomId().isEmpty()){
            error.put("message", "Sensor ID and Room ID is required");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        
        //prevent duplicate sensors
        if(sensors.containsKey(sensor.getId())){
            error.put("message", "Sensor with the same ID already exists");
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
        
        //ensure the linked room exists before creating sensor
        Room room = RoomResource.rooms.get(sensor.getRoomId());
        
        if(room == null){
            throw new LinkedResourceNotFoundException("Room does not exist: " + sensor.getRoomId());
        }
        
        //save sensor
        sensors.put(sensor.getId(), sensor);
        
        //link sensor to the room
        if(room.getSensorIds() != null){
            room.getSensorIds().add(sensor.getId());
        }
        
        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }
    
    //sub resource locator for sensor readings
    @Path("/{id}/readings")
    public SensorReadingResource getReadingResource(@PathParam("id") String id){
        return new SensorReadingResource(id);
    }
    
    //DELETE sensor
    @DELETE
    @Path("/{id}")
    public Response deleteSensor(@PathParam("id") String id){
        
        Sensor sensor = sensors.get(id);
        
        if(sensor == null){
            throw new LinkedResourceNotFoundException("Sensor not found: " + id);
        }
        
        //remove sensor readings for that sensor
        SensorReadingResource.readings.remove(id);
        //remove sensor reference from its room
        Room room = RoomResource.rooms.get(sensor.getRoomId());
        if(room != null && room.getSensorIds() != null){
            room.getSensorIds().remove(id);
        }
        
        sensors.remove(id);
        
        //simple success response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Sensor deleted successfully");
        return Response.ok(response).build();
    }
    
    
    //update sensor status (active, maintenenace)
    @PATCH
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSensorStatus(@PathParam("id") String id, Sensor sensorInput) {

        Sensor sensor = sensors.get(id);

        if (sensor == null) {
            throw new LinkedResourceNotFoundException("Sensor not found: " + id);
        }

        if (sensorInput == null || sensorInput.getStatus() == null || sensorInput.getStatus().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Status must be provided in request body");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        String status = sensorInput.getStatus().toUpperCase();

        if (!status.equals("ACTIVE") &&
            !status.equals("MAINTENANCE") &&
            !status.equals("OFFLINE")) {

            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid status. Allowed values: ACTIVE, MAINTENANCE, OFFLINE");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        sensor.setStatus(status);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sensor status updated successfully");
        response.put("sensorId", id);
        response.put("status", status);

        return Response.ok(response).build();
    }

}
