package com.example.maintenance.entity;


import com.example.maintenance.entity.Enum.IssueCategory;
import com.example.maintenance.entity.Enum.IssuePriority;
import com.example.maintenance.entity.Enum.IssueStatus;
import jakarta.persistence.*;

@Entity
@Table(name="Issue")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String location;
    private String createdAT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CreatedBy", nullable = false)
    private User createdBy;

    @Enumerated(EnumType.STRING)
    private IssueCategory issueCategory;

    @Enumerated(EnumType.STRING)
    private IssuePriority issuePriority;

    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;


    public Long getId()
    {
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

//    public void setCreatedAT(String createdAT) {
//        this.createdAT = createdAT;
//    }

//    public String getCreatedAT() {
//        return createdAT;
//    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public IssueCategory getIssueCategory() {
        return issueCategory;
    }

    public IssuePriority getIssuePriority() {
        return issuePriority;
    }

    public IssueStatus getIssueStatus() {
        return issueStatus;
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

    public void setLocation(String location) {
        this.location = location;
    }
    public Issue() {
    }

}
