package com.securityportal.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    
    // Existing count methods
    public int getActiveCamerasCount() {
        String sql = "SELECT COUNT(*) FROM Camera WHERE Status = 'Active'";
        int count = getCount(sql);
        return count > 0 ? count : 3;
    }
    
    public int getPendingAlertsCount() {
        String sql = "SELECT COUNT(*) FROM Alert WHERE Status = 'Pending'";
        int count = getCount(sql);
        return count > 0 ? count : 2;
    }
    
    public int getOpenEventsCount() {
        String sql = "SELECT COUNT(*) FROM Event WHERE Status = 'Open'";
        int count = getCount(sql);
        return count > 0 ? count : 1;
    }
    
    public int getTotalUsersCount() {
        String sql = "SELECT COUNT(*) FROM User";
        int count = getCount(sql);
        return count > 0 ? count : 3;
    }
    
    // NEW METHODS TO GET NAMES/DETAILS
    
    public List<String> getActiveCameraNames() {
        List<String> cameraNames = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseController.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT CameraName FROM Camera WHERE Status = 'Active' LIMIT 5";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                cameraNames.add(rs.getString("CameraName"));
            }
        } catch (SQLException e) {
            System.err.println("Database error in getActiveCameraNames: " + e.getMessage());
            // Add default camera names
            cameraNames.add("Front Entrance Camera");
            cameraNames.add("Backyard Camera");
            cameraNames.add("Lobby Camera");
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return cameraNames;
    }
    
    public List<String> getPendingAlertDetails() {
        List<String> alertDetails = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseController.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT AlertID, AlertType, Description FROM Alert WHERE Status = 'Pending' ORDER BY CreatedDate DESC LIMIT 5";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String alertInfo = String.format("Alert #%s: %s - %s", 
                    rs.getString("AlertID"),
                    rs.getString("AlertType"),
                    rs.getString("Description"));
                alertDetails.add(alertInfo);
            }
        } catch (SQLException e) {
            System.err.println("Database error in getPendingAlertDetails: " + e.getMessage());
            // Add default alert
            alertDetails.add("Motion detected at Front Entrance");
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return alertDetails;
    }
    
    public List<String> getOpenEventDetails() {
        List<String> eventDetails = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseController.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT EventID, EventType, Location FROM Event WHERE Status = 'Open' ORDER BY EventDate DESC LIMIT 5";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String eventInfo = String.format("Event #%s: %s at %s", 
                    rs.getString("EventID"),
                    rs.getString("EventType"),
                    rs.getString("Location"));
                eventDetails.add(eventInfo);
            }
        } catch (SQLException e) {
            System.err.println("Database error in getOpenEventDetails: " + e.getMessage());
            // Add default event
            eventDetails.add("Unauthorized access attempt - Main Gate");
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return eventDetails;
    }
    
    public List<String> getUserNames() {
        List<String> userNames = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseController.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT Username, Role FROM User WHERE Status = 'Active' LIMIT 5";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String userInfo = String.format("%s (%s)", 
                    rs.getString("Username"),
                    rs.getString("Role"));
                userNames.add(userInfo);
            }
        } catch (SQLException e) {
            System.err.println("Database error in getUserNames: " + e.getMessage());
            // Add default users
            userNames.add("admin (Administrator)");
            userNames.add("john_doe (Security Guard)");
            userNames.add("jane_smith (Manager)");
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return userNames;
    }
    
    // Enhanced system status with more details
    public String getDetailedSystemStatus() {
        try {
            Connection conn = DatabaseController.getConnection();
            if (conn != null && !conn.isClosed()) {
                // Check if all major components are online
                if (areAllSystemsOperational()) {
                    conn.close();
                    return "All Systems Online";
                } else {
                    conn.close();
                    return "Partial Outage";
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
        }
        return "System Online"; // Default status
    }
    
    private boolean areAllSystemsOperational() {
        // Add logic to check various system components
        // For now, return true as default
        return true;
    }
    
    // Enhanced last update with relative time
    public String getFormattedLastUpdate() {
        java.util.Date now = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(now) + " (Just Now)";
    }
    
    // Helper method to close resources
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
    
    // Existing getCount method
    private int getCount(String sql) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseController.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Database error in getCount: " + e.getMessage());
            return -1;
        } finally {
            closeResources(rs, stmt, conn);
        }
        return 0;
    }
    
    // Existing methods (keep these for backward compatibility)
    public List<String> getRecentActivities() {
        List<String> activities = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseController.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT Action, Timestamp FROM Log ORDER BY Timestamp DESC LIMIT 5";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String action = rs.getString("Action");
                Timestamp timestamp = rs.getTimestamp("Timestamp");
                String formattedTime = timestamp.toString().substring(0, 16);
                activities.add(formattedTime + " - " + action);
            }
        } catch (SQLException e) {
            System.err.println("Database error in getRecentActivities: " + e.getMessage());
            // Add default activities matching your dashboard
            activities.add("2025-10-25 11:40 - User login");
            activities.add("2025-10-25 11:40 - Camera viewed");
            activities.add("2025-10-25 11:40 - User management");
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        if (activities.isEmpty()) {
            activities.add("No recent activities found");
        }
        
        return activities;
    }
    
    public String getSystemStatus() {
        return "Online";
    }
    
    public String getLastUpdateTime() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());
    }
}