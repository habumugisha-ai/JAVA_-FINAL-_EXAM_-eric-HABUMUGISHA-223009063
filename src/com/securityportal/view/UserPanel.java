package com.securityportal.view;

import javax.swing.*;
import java.awt.*;
import com.securityportal.util.ColorScheme;

public class UserPanel extends JPanel {
    public UserPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.DARK_BG);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // User list section
        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(Color.WHITE);
        userListPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel userListTitle = new JLabel("User ID");
        userListTitle.setFont(new Font("Arial", Font.BOLD, 16));
        userListTitle.setForeground(ColorScheme.DARK_BG);
        
        // User list items
        JLabel adminUser = new JLabel("• admin");
        JLabel securityUser = new JLabel("• security1");
        JLabel operatorUser = new JLabel("• operator1");
        
        // Style the user labels
        Font userFont = new Font("Arial", Font.PLAIN, 14);
        adminUser.setFont(userFont);
        securityUser.setFont(userFont);
        operatorUser.setFont(userFont);
        
        adminUser.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        securityUser.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        operatorUser.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));

        userListPanel.add(userListTitle);
        userListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userListPanel.add(adminUser);
        userListPanel.add(securityUser);
        userListPanel.add(operatorUser);

        // Table section
        String[] columns = {"User ID", "Username", "Full Name", "Role", "Last Login"};
        Object[][] data = {
            {1, "admin", "HABUMUGISHA ERIC", "Admin", "2024-01-15 10:30:00"},
            {2, "security1", "TUYIZERE ely", "Security", "2024-01-15 09:15:00"},
            {3, "operator1", "M.CLOUDINE", "Operator", "2024-01-14 16:45:00"}
        };

        JTable userTable = new JTable(data, columns);
        userTable.setRowHeight(30);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ColorScheme.LIGHT_BG);
        contentPanel.add(userListPanel, BorderLayout.NORTH);
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}