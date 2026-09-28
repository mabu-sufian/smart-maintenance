package com.example.maintenance.dto;

import com.example.maintenance.entity.Enum.IssuePriority;

public class UpdatePriorityRequest {

    private IssuePriority issuePriority;

    public IssuePriority getIssuePriority() {
        return issuePriority;
    }

    public void setIssuePriority(IssuePriority issuePriority) {
        this.issuePriority = issuePriority;
    }
}
