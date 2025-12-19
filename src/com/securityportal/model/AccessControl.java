package com.securityportal.model;

import java.time.LocalDateTime;

public class AccessControl {
    private int accessControlID;
    private String accessLevel;
    private String description;
    private LocalDateTime createdAt;

    public AccessControl() {}

    public AccessControl(int accessControlID, String accessLevel, String description, LocalDateTime createdAt) {
        this.accessControlID = accessControlID;
        this.accessLevel = accessLevel;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getAccessControlID() { return accessControlID; }
    public void setAccessControlID(int accessControlID) { this.accessControlID = accessControlID; }
    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}