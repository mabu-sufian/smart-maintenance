package com.example.maintenance.dto;

import com.example.maintenance.entity.Enum.IssueStatus;

public class IssueStatusUpdateRequest {
    private IssueStatus Status;

    public IssueStatus getStatus() {
        return Status;
    }

    public void setStatus(IssueStatus status) {
        Status = status;
    }
}
