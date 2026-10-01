package com.example.maintenance.exception;

public class AlreadyRegisteredException extends RuntimeException{
    public AlreadyRegisteredException(String message)
    {
        super(message);
    }
}
