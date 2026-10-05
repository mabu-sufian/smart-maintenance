package com.example.maintenance.dto;

import com.example.maintenance.entity.User;

import java.time.LocalDateTime;

public class EmergencyResponseDTO {

    private Long id;
    private String title;
    private String Description;
    private Long IssueId;
    private String Location;
    private String emergencySeverity;
    private String emergencyStatus;
    private RegisteredResponseDTO user;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getIssueId() {
        return IssueId;
    }

    public void setIssueId(Long issueId) {
        IssueId = issueId;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getEmergencySeverity() {
        return emergencySeverity;
    }

    public void setEmergencySeverity(String emergencySeverity) {
        this.emergencySeverity = emergencySeverity;
    }

    public String getEmergencyStatus() {
        return emergencyStatus;
    }

    public void setEmergencyStatus(String emergencyStatus) {
        this.emergencyStatus = emergencyStatus;
    }

    public RegisteredResponseDTO getUser() {
        return user;
    }

    public void setUser(RegisteredResponseDTO user) {
        this.user = user;
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
