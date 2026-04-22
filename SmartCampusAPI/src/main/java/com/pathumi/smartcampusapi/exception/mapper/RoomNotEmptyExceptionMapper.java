package com.pathumi.smartcampusapi.exception.mapper;

//maps RoomNotEmptyException to an HTTP response so clients receive a proper JSON error instead of a server crash
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;
import com.pathumi.smartcampusapi.exceptions.RoomNotEmptyException;

@Provider //registers this as a global exception handler
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException>{
    
    @Override
    public Response toResponse(RoomNotEmptyException ex){
        
        //create simple JSON error response
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("status", Response.Status.CONFLICT.getStatusCode());
        
        //409 CONFLICT -> request is valid but conflicts with current resource state
        return Response.status(Response.Status.CONFLICT)
                .entity(error)
                .type("application/json") //ensures response is sent as JSON
                .build();
  
    }
}
