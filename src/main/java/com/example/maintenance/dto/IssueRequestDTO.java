package com.example.maintenance.dto;

import com.example.maintenance.model.IssueCategory;
import com.example.maintenance.model.IssuePriority;
import com.example.maintenance.model.IssueStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IssueRequestDTO {

    @NotBlank(message = "title not be blank")
    @Size(max=50)
    private String title;

    @Size(min =5, max=500, message = "give broad description")
    private String description;

    @NotNull
    private String location;
    private IssueCategory issueCategory;
    private IssuePriority issuePriority;


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public IssueCategory getIssueCategory() {
        return issueCategory;
    }

    public IssuePriority getIssuePriority() {
        return issuePriority;
    }


    public void setIssueCategory(IssueCategory issueCategory) {
        this.issueCategory = issueCategory;
    }

    public void setIssuePriority(IssuePriority issuePriority) {
        this.issuePriority = issuePriority;
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
}
