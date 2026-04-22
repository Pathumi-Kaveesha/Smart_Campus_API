package com.pathumi.smartcampusapi.exceptions;

//custom runtime exception used when a room, sensor cannot be found 
public class LinkedResourceNotFoundException extends RuntimeException {
    public LinkedResourceNotFoundException(String message){
        super(message); //passes error messages to parent RunTimeException
    }
}
