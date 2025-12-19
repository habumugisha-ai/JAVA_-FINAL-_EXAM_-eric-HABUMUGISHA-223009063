package com.securityportal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.securityportal.view.LoginForm;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
      
            public void run() {
                try {
                    // Use the cross-platform look and feel instead
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            }
        });
    }
}