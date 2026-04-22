package com.pathumi.smartcampusapi.exception.mapper;

//converts LinkedResourceNotFoundException into an HTTP response so the API returns a proper JSON error instead of crashing
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

import com.pathumi.smartcampusapi.exceptions.LinkedResourceNotFoundException;

@Provider //registers this class as a global exception handler
public class LinkedResourceNotFoundExceptionMapper implements  ExceptionMapper<LinkedResourceNotFoundException>{
    
    @Override
    public Response toResponse(LinkedResourceNotFoundException ex){
        
        //JSON-style error response body
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("status", 422);
        
        //422 used when request is valid but resource state is invalid/missing
        return Response.status(422) //Unprocessable Entity
                .entity(error)
                .type("application/json")
                .build();
    }
    
}
