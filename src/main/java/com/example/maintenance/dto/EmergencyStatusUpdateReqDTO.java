package com.example.maintenance.dto;

import com.example.maintenance.entity.Enum.EmergencyStatus;

public class EmergencyStatusUpdateReqDTO {

    private EmergencyStatus Status;

    public EmergencyStatus getStatus() {
        return Status;
    }

    public void setStatus(EmergencyStatus status) {
        Status = status;
    }
}
