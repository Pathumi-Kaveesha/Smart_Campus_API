package com.pathumi.smartcampusapi.exception.mapper;

//maps SensorUnavailableException to an HTTP response so clients receive a proper error messafe instead of a server crash
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;
import com.pathumi.smartcampusapi.exceptions.SensorUnavailableException;

@Provider //registers this mapper globally
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    
    @Override
    public Response toResponse(SensorUnavailableException ex){
        
        //create simple JSON error response
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("status", Response.Status.FORBIDDEN.getStatusCode());
        
        //403 FORBIDDEN -> sensor exist but cannot be used
        return Response.status(Response.Status.FORBIDDEN)
                .entity(error)
                .type("application/json")
                .build();
    }    
}
