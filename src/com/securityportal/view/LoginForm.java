package com.securityportal.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.securityportal.controller.AuthController;
import com.securityportal.util.ColorScheme;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private AuthController authController;

    public LoginForm() {
        this.authController = new AuthController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Security Portal Management System47 - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 550); // Increased height to accommodate new button
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with background color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(ColorScheme.PRIMARY_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JLabel headerLabel = new JLabel("SECURITY PORTAL", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Login form panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(0, 1, 10, 10));
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Username field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 35));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Password field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 35));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Login button
        loginButton = new JButton("LOGIN");
        loginButton.setBackground(ColorScheme.SUCCESS_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Forgot Password button
        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setBackground(ColorScheme.PRIMARY_COLOR);
        forgotPasswordButton.setForeground(Color.WHITE);
        forgotPasswordButton.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.setContentAreaFilled(false); // Make transparent
        forgotPasswordButton.setBorderPainted(false); // Remove border

        // Add components to login panel
        loginPanel.add(userLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passLabel);
        loginPanel.add(passwordField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPanel.add(forgotPasswordButton);

        // Add panels to main panel
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);

        // Add action listeners
        loginButton.addActionListener(new LoginAction());
        passwordField.addActionListener(new LoginAction());
        forgotPasswordButton.addActionListener(new ForgotPasswordAction());

        add(mainPanel);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginForm.this, 
                    "Please enter both username and password", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (authController.login(username, password)) {
                dispose();
                new Dashboard(authController).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(LoginForm.this, 
                    "Invalid username or password", 
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        }
    }

    private class ForgotPasswordAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showForgotPasswordDialog();
        }
    }

    private void showForgotPasswordDialog() {
        // Create a custom dialog for password recovery
        JDialog forgotDialog = new JDialog(this, "Password Recovery", true);
        forgotDialog.setSize(350, 200);
        forgotDialog.setLocationRelativeTo(this);
        forgotDialog.setLayout(new BorderLayout());
        forgotDialog.setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Instruction label
        JLabel instructionLabel = new JLabel(
            "<html><div style='text-align: center;'>" +
            "Enter your username to receive password reset instructions via email." +
            "</div></html>"
        );
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Username input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel usernameLabel = new JLabel("Username:");
        JTextField recoveryUsernameField = new JTextField(15);
        inputPanel.add(usernameLabel);
        inputPanel.add(recoveryUsernameField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        submitButton.setBackground(ColorScheme.SUCCESS_COLOR);
        submitButton.setForeground(Color.WHITE);
        cancelButton.setBackground(ColorScheme.SECONDARY_COLOR);
        cancelButton.setForeground(Color.WHITE);

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        // Add components to main panel
        mainPanel.add(instructionLabel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        forgotDialog.add(mainPanel);

        // Add action listeners for dialog buttons
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = recoveryUsernameField.getText().trim();
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(forgotDialog, 
                        "Please enter your username", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Call the authentication controller to handle password reset
                boolean success = authController.initiatePasswordReset(username);
                
                if (success) {
                    JOptionPane.showMessageDialog(forgotDialog,
                        "Password reset instructions have been sent to your registered email address.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    forgotDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(forgotDialog,
                        "Username not found or email not registered.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forgotDialog.dispose();
            }
        });

        // Add Enter key support for the recovery username field
        recoveryUsernameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitButton.doClick();
            }
        });

        forgotDialog.setVisible(true);
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}