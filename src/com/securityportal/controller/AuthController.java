package com.securityportal.controller;

import com.securityportal.model.User;

public class AuthController {
    private DatabaseController dbController;
    private User currentUser;

    public AuthController() {
        this.dbController = new DatabaseController();
    }

    public boolean login(String username, String password) {
        User user = dbController.authenticateUser(username, password);
        if (user != null) {
            this.currentUser = user;
            dbController.updateLastLogin(user.getUserID());
            return true;
        }
        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // ADD THIS METHOD TO REFRESH USER DATA
    public void refreshCurrentUser() {
        if (currentUser != null) {
            // Get fresh user data from database
            User refreshedUser = dbController.getUserById(currentUser.getUserID());
            if (refreshedUser != null) {
                this.currentUser = refreshedUser;
                System.out.println("User data refreshed: " + refreshedUser.getFullName());
            }
        }
    }

    // ALTERNATIVE: Refresh by username
    public void refreshCurrentUserByUsername() {
        if (currentUser != null) {
            User refreshedUser = dbController.getUserByUsername(currentUser.getUsername());
            if (refreshedUser != null) {
                this.currentUser = refreshedUser;
            }
        }
    }
}