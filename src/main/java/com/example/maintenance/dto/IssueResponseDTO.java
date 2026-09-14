package com.example.maintenance.dto;

import com.example.maintenance.model.IssueCategory;
import com.example.maintenance.model.IssuePriority;
import com.example.maintenance.model.IssueStatus;

public class IssueResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String location;
    private IssueCategory issueCategory;
    private IssuePriority issuePriority;
    private IssueStatus issueStatus;

    public Long getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setIssueCategory(IssueCategory issueCategory) {
        this.issueCategory = issueCategory;
    }

    public void setIssuePriority(IssuePriority issuePriority) {
        this.issuePriority = issuePriority;
    }

    public void setIssueStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
    }
}
