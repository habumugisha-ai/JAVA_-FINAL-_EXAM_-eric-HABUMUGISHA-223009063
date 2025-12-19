package com.securityportal.model;

import java.time.LocalDateTime;

public class Camera {
    private int cameraID;
    private String cameraName;
    private String location;
    private String status;
    private LocalDateTime createdAt;

    public Camera() {}

    public Camera(int cameraID, String cameraName, String location, String status, LocalDateTime createdAt) {
        this.cameraID = cameraID;
        this.cameraName = cameraName;
        this.location = location;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getCameraID() { return cameraID; }
    public void setCameraID(int cameraID) { this.cameraID = cameraID; }
    public String getCameraName() { return cameraName; }
    public void setCameraName(String cameraName) { this.cameraName = cameraName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}