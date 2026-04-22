package com.pathumi.smartcampusapi.filter;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.ext.Provider; 

import java.io.IOException;
import java.util.logging.Logger;

@Provider //registeres this filter globally for all API requests/responses
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter{
    
    //Logger used to print request and response details
    private static final Logger logger = Logger.getLogger(LoggingFilter.class.getName());
    
    private static final String START_TIME = "start-time";
    
    //REQUEST logging
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException{
        //logs HTTP method and requested URL
        logger.info("Incoming Request: " +
                requestContext.getMethod() + " " +
                requestContext.getUriInfo().getRequestUri());
        
        //store start time for performance tracking
        requestContext.setProperty(START_TIME, System.currentTimeMillis());
    }
    
    //RESPONSE logging
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException{
        
        Long startTime = (Long) requestContext.getProperty(START_TIME);
        long duration = (startTime == null) ? -1 : System.currentTimeMillis() - startTime;
        
        //logs response status code
        logger.info("Response Status: " + responseContext.getStatus());
        logger.info("Response Time: " + duration + " ms");
    }
   
}
