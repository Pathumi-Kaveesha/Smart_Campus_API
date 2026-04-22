package com.pathumi.smartcampusapi.resources;

//JAX-RS annotations for defining endpoints
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

//used to define response format as JSON
import javax.ws.rs.core.MediaType;

//utilities for building JSON response
import java.util.HashMap;
import java.util.Map;


@Path("/")
public class DiscoveryResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getApiInfo(){
        Map<String, Object> response = new HashMap<>();
        
        response.put("api", "Smart Campus API");
        response.put("version", "v1");
        response.put("contact", "admin@university.com");
        response.put("status", "running");
        
        Map<String, Object> endpoints = new HashMap<>();
        
        endpoints.put("rooms", "/api/v1/rooms");
        endpoints.put("sensors", "/api/v1/sensors");
        endpoints.put("readings", "/api/v1/sensors/{id}/readings");
        
        response.put("endpoints", endpoints);
        
        return response;
    }
    
}
