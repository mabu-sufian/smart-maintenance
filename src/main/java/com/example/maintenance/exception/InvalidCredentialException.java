package com.example.maintenance.exception;

public class InvalidCredentialException extends RuntimeException{
    public InvalidCredentialException(String message)
    {
        super(message);
    }
}
