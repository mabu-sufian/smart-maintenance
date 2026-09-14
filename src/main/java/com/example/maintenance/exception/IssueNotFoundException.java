package com.example.maintenance.exception;

public class IssueNotFoundException extends RuntimeException{
    public IssueNotFoundException(String message)
    {
        super(message);
    }
}
