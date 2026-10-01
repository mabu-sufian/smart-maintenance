package com.example.maintenance.exception;

public class WrongTechnicianException  extends RuntimeException{
    public WrongTechnicianException(String msg)
    {
        super(msg);
    }
}
