package com.pathumi.smartcampusapi.exceptions;

//custom exception thrown when a sensor cannot be used (undner maintenance)
public class SensorUnavailableException extends RuntimeException{
    public SensorUnavailableException(String message){
        super(message); //passes error messages to parent RunTimeException
    }
}
