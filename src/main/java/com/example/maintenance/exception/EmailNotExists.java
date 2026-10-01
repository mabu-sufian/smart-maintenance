package com.example.maintenance.exception;

public class EmailNotExists extends RuntimeException{

    public EmailNotExists(String message)
    {
        super(message);
    }
}
