package com.example.maintenance.dto;

import com.example.maintenance.entity.Enum.EmergencySeverity;

public class EmergencyRequestDTO {
    private String description;
    private EmergencySeverity severrity;
    public EmergencySeverity getSeverrity() {
        return severrity;
    }

    public void setSeverrity(EmergencySeverity severrity) {
        this.severrity = severrity;
    }






    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
