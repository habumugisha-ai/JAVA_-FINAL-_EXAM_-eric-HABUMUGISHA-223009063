package com.securityportal.controller;

import java.sql.*;
import com.securityportal.model.User;
import com.securityportal.util.PasswordHasher;

public class DatabaseController {
    private static final String URL = "jdbc:mysql://localhost:3306/security_portal";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw e;
        }
    }

    public User authenticateUser(String username, String password) {
        // For testing - direct authentication without database
        if ("admin".equals(username) && "admin123".equals(password)) {
            return new User(1, "admin", "hash", "admin@security.com", 
                           "HABUMUGISHA ERIC", "Admin", 
                           java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        }
        if ("security1".equals(username) && "admin123".equals(password)) {
            return new User(2, "security1", "hash", "security1@company.com", 
                           "TUYIZERE ely", "Security", 
                           java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        }
        if ("operator1".equals(username) && "admin123".equals(password)) {
            return new User(3, "operator1", "hash", "operator@company.com", 
                           "M.CLOUDINE", "Operator", 
                           java.time.LocalDateTime.now(), java.time.LocalDateTime.now());
        }
        return null;
    }

    public void updateLastLogin(int userID) {
        // Simple implementation for now
        System.out.println("Last login updated for user: " + userID);
    }
}