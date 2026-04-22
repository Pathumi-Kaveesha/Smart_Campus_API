package com.pathumi.smartcampusapi.resources;

//JAX-RS annotations for defining REST endpoints
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

//respomse handling
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//collection and map utilities for in-memory data storage
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//models
import com.pathumi.smartcampusapi.models.Room;

//custom exceptions
import com.pathumi.smartcampusapi.exceptions.LinkedResourceNotFoundException;
import com.pathumi.smartcampusapi.exceptions.RoomNotEmptyException;
import com.pathumi.smartcampusapi.models.Sensor;
import static com.pathumi.smartcampusapi.resources.SensorResource.sensors;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {
    
    public static Map<String, Room> rooms = new HashMap<>();
    
    //GET all rooms
    @GET
    public Response getAllRooms(){
        if(rooms.isEmpty()){
            Map<String, String> error = new HashMap<>();
            error.put("message", "No rooms available");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(rooms.values()).build();
    }
    
    //POST create room
    @POST
    public Response createRoom(Room room){
        
        //common map to store error messages to be printed as json
        Map<String, String> error = new HashMap<>();
        
        //basic validation before saving
        if(room == null || room.getId() == null || room.getId().isEmpty()){
            error.put("message", "Room ID is required");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        
        //prevent duplicate room IDs
        if(rooms.containsKey(room.getId())){
            error.put("message", "Room with this ID already exists");
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
        
        rooms.put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }
    
    //GET room by id
    @GET
    @Path("/{id}")
    public Response getRoom(@PathParam("id") String id){
        Room room = rooms.get(id);
        
        //throw error uf room not found
        if(room == null) {
            throw new LinkedResourceNotFoundException("Room not found: " + id);
        }
        return Response.ok(room).build();
    }
    
    //DELETE room
    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id){
        
        Room room = rooms.get(id);
        
        if(room == null){
            throw new LinkedResourceNotFoundException("Room not found: " + id);
        }
        
        //prevent deletion if any sensor exists in the room
        boolean hasAnySensor = room.getSensorIds().stream()
                .map(SensorResource.sensors::get)
                .anyMatch(Objects::nonNull);
        
        if(hasAnySensor){
            throw new RoomNotEmptyException("Room cannot be deleted. Sensors are still assigned.");
        }
        
        rooms.remove(id);
        
        Map<String, String> error = new HashMap<>();
        error.put("message", "Room deleted successfully");
        return Response.status(Response.Status.OK).entity(error).build();
    }
    
    //GET sensors by room ID
    @GET
    @Path("/{id}/sensors")
    public Response getSensorsByRoom(@PathParam("id") String roomId) {

        //validate room exists first
        Room room = RoomResource.rooms.get(roomId);

        if (room == null) {
            throw new LinkedResourceNotFoundException("Room does not exist: " + roomId);
        }

        //collect sensors
        List<Sensor> result = new ArrayList<>();

        for (Sensor s : sensors.values()) {
            if (roomId.equalsIgnoreCase(s.getRoomId())) {
                result.add(s);
            }
        }

        //if room exists but has no sensors
        if (result.isEmpty()) {
            Map<String, String> msg = new HashMap<>();
            msg.put("message", "No sensors assigned to this room");

            return Response.ok(msg).build();
        }

        return Response.ok(result).build();
    }
    
   
}
