package com.example.maintenance.entity;


import com.example.maintenance.entity.Enum.EmergencySeverity;
import com.example.maintenance.entity.Enum.EmergencyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.cglib.core.Local;
import org.springframework.data.repository.cdi.Eager;

import java.time.LocalDateTime;

@Entity
@Table(name="emergencies")
public class Emergency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="issue_id", nullable = false)
    private Issue issue;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(nullable = false)
    private String title;

    @NotNull(message = "Description is required")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmergencySeverity emergencySeverity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmergencyStatus emergencyStatus;

    @ManyToOne
    @JoinColumn(name="reported_By", nullable = false)
    private User reportedBy;
    private String Location;

    private LocalDateTime createdAt;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime assignedAt;
    private LocalDateTime responseStartedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmergencySeverity getEmergencySeverity() {
        return emergencySeverity;
    }

    public void setEmergencySeverity(EmergencySeverity emergencySeverity) {
        this.emergencySeverity = emergencySeverity;
    }

    public EmergencyStatus getEmergencyStatus() {
        return emergencyStatus;
    }

    public void setEmergencyStatus(EmergencyStatus emergencyStatus) {
        this.emergencyStatus = emergencyStatus;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public void setAcknowledgedAt(LocalDateTime acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getResponseStartedAt() {
        return responseStartedAt;
    }

    public void setResponseStartedAt(LocalDateTime responseStartedAt) {
        this.responseStartedAt = responseStartedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }
}
