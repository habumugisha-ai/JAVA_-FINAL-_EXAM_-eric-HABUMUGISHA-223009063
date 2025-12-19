package com.securityportal.model;

import java.time.LocalDateTime;

public class Event {
    private int eventID;
    private int referenceID;
    private String description;
    private LocalDateTime date;
    private String status;
    private String remarks;

    public Event() {}

    public Event(int eventID, int referenceID, String description, LocalDateTime date, String status, String remarks) {
        this.eventID = eventID;
        this.referenceID = referenceID;
        this.description = description;
        this.date = date;
        this.status = status;
        this.remarks = remarks;
    }

    // Getters and Setters
    public int getEventID() { return eventID; }
    public void setEventID(int eventID) { this.eventID = eventID; }
    public int getReferenceID() { return referenceID; }
    public void setReferenceID(int referenceID) { this.referenceID = referenceID; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}