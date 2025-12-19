package com.securityportal.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.securityportal.controller.AuthController;
import com.securityportal.controller.DashboardController;
import com.securityportal.util.ColorScheme;

public class Dashboard extends JFrame {
    private AuthController authController;
    private DashboardController dashboardController;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public Dashboard(AuthController authController) {
        this.authController = authController;
        this.dashboardController = new DashboardController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Security Portal Management System47 - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        JPanel sidebarPanel = createSidebarPanel();

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Add all panels including the new ReportPanel
        mainPanel.add(createDashboardPanel(), "dashboard");
        mainPanel.add(new UserPanel(), "users");
        mainPanel.add(new CameraPanel(), "cameras");
        mainPanel.add(new EventPanel(), "events");
        mainPanel.add(new AlertPanel(), "alerts");
        mainPanel.add(new AccessControlPanel(), "access");
        mainPanel.add(new ReportPanel(), "reports"); // Add reports panel

        add(headerPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "dashboard");
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ColorScheme.DARK_BG);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Security Portal Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JLabel userInfo = new JLabel("Welcome, " + authController.getCurrentUser().getFullName() + 
                                   " (" + authController.getCurrentUser().getRole() + ")");
        userInfo.setForeground(Color.WHITE);
        userInfo.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(ColorScheme.ACCENT_COLOR);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authController.logout();
                dispose();
                new LoginForm().setVisible(true);
            }
        });

        userPanel.add(userInfo);
        userPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        userPanel.add(logoutButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(ColorScheme.PRIMARY_COLOR);
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Updated menu items including Reports
        String[] menuItems = {"Dashboard", "Users", "Cameras", "Events", "Alerts", "Access Control", "Reports"};
        String[] cardNames = {"dashboard", "users", "cameras", "events", "alerts", "access", "reports"};

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], cardNames[i]);
            sidebarPanel.add(menuButton);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        sidebarPanel.add(Box.createVerticalGlue());
        return sidebarPanel;
    }

    private JButton createMenuButton(String text, final String cardName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 45));
        button.setBackground(ColorScheme.SECONDARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, cardName);
            }
        });
        return button;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(ColorScheme.LIGHT_BG);
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel statsPanel = createStatsPanel();
        dashboardPanel.add(statsPanel, BorderLayout.NORTH);

        JTextArea activityArea = new JTextArea();
        activityArea.setText("Recent System Activity:\n\n");
        
        List<String> activities = dashboardController.getRecentActivities();
        for (String activity : activities) {
            activityArea.append("• " + activity + "\n");
        }
        
        activityArea.setEditable(false);
        activityArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(activityArea);
        dashboardPanel.add(scrollPane, BorderLayout.CENTER);

        return dashboardPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBackground(ColorScheme.LIGHT_BG);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        String[] stats = {"Active Cameras", "Pending Alerts", "Open Events", "Total Users", "System Status", "Last Update"};
        String[] values = {
            String.valueOf(dashboardController.getActiveCamerasCount()),
            String.valueOf(dashboardController.getPendingAlertsCount()),
            String.valueOf(dashboardController.getOpenEventsCount()),
            String.valueOf(dashboardController.getTotalUsersCount()),
            "Online", 
            "Just Now"
        };
        Color[] colors = {
            ColorScheme.SUCCESS_COLOR, ColorScheme.WARNING_COLOR, 
            ColorScheme.ACCENT_COLOR, ColorScheme.PRIMARY_COLOR, 
            ColorScheme.SUCCESS_COLOR, ColorScheme.SECONDARY_COLOR
        };

        for (int i = 0; i < stats.length; i++) {
            statsPanel.add(createStatCard(stats[i], values[i], colors[i]));
        }

        return statsPanel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(ColorScheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.DARK_GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }
}