package com.example.maintenance.dto;

import com.example.maintenance.entity.Enum.IssueCategory;

public class UpdateCategoryRequest {

    private IssueCategory issueCategory;

    public IssueCategory getIssueCategory() {
        return issueCategory;
    }

    public void setIssueCategory(IssueCategory issueCategory) {
        this.issueCategory = issueCategory;
    }
}
