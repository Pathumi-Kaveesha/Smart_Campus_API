package com.pathumi.smartcampusapi.exceptions;

//custom exception thrown when attempting to delete a room that still has active sensors
public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String message){
        super(message); //passes error messages to parent RunTimeException
    }
}
