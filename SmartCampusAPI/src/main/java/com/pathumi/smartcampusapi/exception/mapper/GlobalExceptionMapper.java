package com.pathumi.smartcampusapi.exception.mapper;

//JAX-RS classes for handling global exceptions and building HTTP responses
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//utilities for creating JSON error response
import java.util.HashMap;
import java.util.Map;

@Provider // registers this as a global fallback exception handler
public class GlobalExceptionMapper implements ExceptionMapper<Throwable>{
    
    @Override
    public Response toResponse(Throwable ex){
        
        //generic error response sent to client
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Internal server error. Please contact support");
        error.put("status", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        
        //500 INTERNAL SERVER ERROR for unexpected exceptions
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .type("application/json")
                .build();
    }
}
