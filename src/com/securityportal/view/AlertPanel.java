package com.securityportal.view;

import com.securityportal.model.Alert;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AlertPanel extends JPanel {
    private JTable alertTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilter;
    private JComboBox<String> severityFilter;
    
    public AlertPanel() {
        initializeUI();
        loadAlertData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Alert Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Status:"));
        statusFilter = new JComboBox<>(new String[]{"All", "Pending", "Acknowledged", "Resolved", "Escalated"});
        statusFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterAlerts();
            }
        });
        filterPanel.add(statusFilter);
        
        filterPanel.add(new JLabel("Filter by Severity:"));
        severityFilter = new JComboBox<>(new String[]{"All", "Critical", "High", "Medium", "Low"});
        severityFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterAlerts();
            }
        });
        filterPanel.add(severityFilter);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton acknowledgeBtn = createStyledButton("Acknowledge", new Color(0, 123, 255));
        JButton resolveBtn = createStyledButton("Resolve", new Color(40, 167, 69));
        JButton escalateBtn = createStyledButton("Escalate", new Color(220, 53, 69));
        JButton refreshBtn = createStyledButton("Refresh", new Color(23, 162, 184));
        JButton addAlertBtn = createStyledButton("Add Alert", new Color(111, 66, 193));
        
        buttonPanel.add(acknowledgeBtn);
        buttonPanel.add(resolveBtn);
        buttonPanel.add(escalateBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(addAlertBtn);

        controlPanel.add(filterPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);

        // Table setup
        String[] columns = {"Alert ID", "Description", "Date", "Status", "Severity", "Action Required", "Assigned To"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        alertTable = new JTable(tableModel);
        alertTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        alertTable.setRowHeight(30);
        alertTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        alertTable.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Custom renderers for status and severity
        alertTable.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());
        alertTable.getColumnModel().getColumn(4).setCellRenderer(new SeverityRenderer());
        
        JScrollPane scrollPane = new JScrollPane(alertTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Security Alerts"));
        scrollPane.setPreferredSize(new Dimension(800, 300));

        // Stats panel
        JPanel statsPanel = createStatsPanel();

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);

        // Add action listeners
        acknowledgeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acknowledgeSelectedAlert();
            }
        });
        
        resolveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolveSelectedAlert();
            }
        });
        
        escalateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                escalateSelectedAlert();
            }
        });
        
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAlerts();
            }
        });
        
        addAlertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewAlert();
            }
        });
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        statsPanel.add(createStatCard("Total Alerts", "18", new Color(0, 123, 255)));
        statsPanel.add(createStatCard("Critical", "3", new Color(220, 53, 69)));
        statsPanel.add(createStatCard("Pending", "5", new Color(255, 193, 7)));
        statsPanel.add(createStatCard("Acknowledged", "8", new Color(23, 162, 184)));
        statsPanel.add(createStatCard("Resolved", "5", new Color(40, 167, 69)));
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.DARK_GRAY);
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void loadAlertData() {
        Object[][] data = {
            {1, "High temperature in server room", "2024-01-16 10:30:00", "Pending", "Critical", "Immediate cooling required", "Unassigned"},
            {2, "Camera offline - Parking Lot B1", "2024-01-15 08:15:00", "Acknowledged", "Medium", "Schedule maintenance", "John Security"},
            {3, "Multiple failed login attempts", "2024-01-14 16:45:00", "Resolved", "Low", "None", "System Admin"},
            {4, "Door forced open - Back Exit", "2024-01-16 14:20:00", "Pending", "Critical", "Immediate investigation", "Unassigned"},
            {5, "Network intrusion detected", "2024-01-16 12:30:00", "Acknowledged", "High", "Block IP and investigate", "Security Team"},
            {6, "Fire alarm test overdue", "2024-01-15 09:00:00", "Pending", "Medium", "Schedule test", "Unassigned"},
            {7, "Unauthorized access - Server Room", "2024-01-14 22:15:00", "Escalated", "Critical", "Management notification", "Security Manager"},
            {8, "Power supply fluctuation", "2024-01-14 18:30:00", "Resolved", "Medium", "Monitor UPS", "IT Support"}
        };
        
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    private void acknowledgeSelectedAlert() {
        int selectedRow = alertTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an alert to acknowledge", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row index to model row index
        int modelRow = alertTable.convertRowIndexToModel(selectedRow);
        
        String alertId = tableModel.getValueAt(modelRow, 0).toString();
        String description = tableModel.getValueAt(modelRow, 1).toString();
        String currentStatus = tableModel.getValueAt(modelRow, 3).toString();
        
        if ("Acknowledged".equals(currentStatus) || "Resolved".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this, 
                "This alert is already " + currentStatus.toLowerCase(), 
                "Alert Already Processed", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simple acknowledgment without dialog
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to acknowledge this alert?\n\n" +
            "Alert ID: " + alertId + "\n" +
            "Description: " + description,
            "Confirm Acknowledgment",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            // Update the alert status
            tableModel.setValueAt("Acknowledged", modelRow, 3);
            tableModel.setValueAt("Under investigation", modelRow, 5);
            tableModel.setValueAt("Current User", modelRow, 6); // In real app, use actual username
            
            JOptionPane.showMessageDialog(this,
                "Alert acknowledged successfully!\n\n" +
                "Alert ID: " + alertId + "\n" +
                "Status updated to: Acknowledged",
                "Alert Acknowledged",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void resolveSelectedAlert() {
        int selectedRow = alertTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an alert to resolve", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row index to model row index
        int modelRow = alertTable.convertRowIndexToModel(selectedRow);
        
        String alertId = tableModel.getValueAt(modelRow, 0).toString();
        String description = tableModel.getValueAt(modelRow, 1).toString();
        String currentStatus = tableModel.getValueAt(modelRow, 3).toString();
        
        if ("Resolved".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this, 
                "This alert is already resolved", 
                "Alert Already Resolved", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Ask for resolution notes
        String resolutionNotes = JOptionPane.showInputDialog(this,
            "Enter resolution notes for Alert #" + alertId + ":\n" + description,
            "Resolution Details",
            JOptionPane.QUESTION_MESSAGE);
        
        if (resolutionNotes != null) {
            if (resolutionNotes.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Resolution notes cannot be empty", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update the alert
            tableModel.setValueAt("Resolved", modelRow, 3);
            tableModel.setValueAt("Completed: " + resolutionNotes, modelRow, 5);
            
            JOptionPane.showMessageDialog(this,
                "Alert resolved successfully!\n\n" +
                "Alert ID: " + alertId + "\n" +
                "Resolution: " + resolutionNotes,
                "Alert Resolved",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void escalateSelectedAlert() {
        int selectedRow = alertTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an alert to escalate", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row index to model row index
        int modelRow = alertTable.convertRowIndexToModel(selectedRow);
        
        String alertId = tableModel.getValueAt(modelRow, 0).toString();
        String description = tableModel.getValueAt(modelRow, 1).toString();
        String currentStatus = tableModel.getValueAt(modelRow, 3).toString();
        
        if ("Escalated".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this, 
                "This alert is already escalated", 
                "Alert Already Escalated", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Escalation level selection
        String[] options = {"Level 1 - Team Lead", "Level 2 - Manager", "Level 3 - Director", "Level 4 - Executive"};
        String escalationLevel = (String) JOptionPane.showInputDialog(this,
            "Select escalation level for Alert #" + alertId + ":\n" + description,
            "Escalate Alert",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (escalationLevel != null) {
            // Update the alert
            tableModel.setValueAt("Escalated", modelRow, 3);
            tableModel.setValueAt("Escalated to " + escalationLevel, modelRow, 5);
            tableModel.setValueAt("Management", modelRow, 6);
            
            JOptionPane.showMessageDialog(this,
                "Alert escalated successfully!\n\n" +
                "Alert ID: " + alertId + "\n" +
                "Escalated to: " + escalationLevel + "\n\n" +
                "Management has been notified.",
                "Alert Escalated",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private JPanel createLabeledField(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }
    
    private void refreshAlerts() {
        // Simulate refreshing alerts
        JOptionPane.showMessageDialog(this, "Alerts refreshed successfully!", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void addNewAlert() {
        final JDialog addDialog = new JDialog();
        addDialog.setTitle("Add New Alert");
        addDialog.setSize(400, 400);
        addDialog.setLocationRelativeTo(this);
        addDialog.setLayout(new BorderLayout());
        addDialog.setModal(true);
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        final JTextField descriptionField = new JTextField();
        formPanel.add(createLabeledField("Description:", descriptionField));
        
        final JComboBox<String> severityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});
        formPanel.add(createLabeledField("Severity:", severityCombo));
        
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "Security", "System", "Network", "Hardware", "Environmental", "Access Control"
        });
        formPanel.add(createLabeledField("Category:", categoryCombo));
        
        JTextArea detailsArea = new JTextArea(3, 20);
        detailsArea.setLineWrap(true);
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        formPanel.add(createLabeledField("Additional Details:", detailsScroll));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createStyledButton("Save Alert", new Color(40, 167, 69));
        JButton cancelBtn = createStyledButton("Cancel", new Color(108, 117, 125));
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (descriptionField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(addDialog, "Please enter alert description", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Add new alert to table
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Object[] newAlert = {
                    tableModel.getRowCount() + 1,
                    descriptionField.getText(),
                    timestamp,
                    "Pending",
                    severityCombo.getSelectedItem(),
                    "Review required",
                    "Unassigned"
                };
                
                tableModel.addRow(newAlert);
                JOptionPane.showMessageDialog(addDialog, "Alert added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                addDialog.dispose();
            }
        });
        
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDialog.dispose();
            }
        });
        
        addDialog.add(formPanel, BorderLayout.CENTER);
        addDialog.add(buttonPanel, BorderLayout.SOUTH);
        addDialog.setVisible(true);
    }
    
    private void filterAlerts() {
        // Simple filtering - in a real application you would implement proper filtering
        JOptionPane.showMessageDialog(this, 
            "Filtering alerts by:\nStatus: " + statusFilter.getSelectedItem() + 
            "\nSeverity: " + severityFilter.getSelectedItem(),
            "Filter Applied",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Custom cell renderer for status
    class StatusRenderer extends DefaultTableCellRenderer {
        public StatusRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null) {
                String status = value.toString();
                switch (status) {
                    case "Pending":
                        c.setBackground(new Color(255, 193, 7)); // Yellow
                        c.setForeground(Color.BLACK);
                        break;
                    case "Acknowledged":
                        c.setBackground(new Color(0, 123, 255)); // Blue
                        c.setForeground(Color.WHITE);
                        break;
                    case "Resolved":
                        c.setBackground(new Color(40, 167, 69)); // Green
                        c.setForeground(Color.WHITE);
                        break;
                    case "Escalated":
                        c.setBackground(new Color(220, 53, 69)); // Red
                        c.setForeground(Color.WHITE);
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                }
            }
            
            return c;
        }
    }
    
    // Custom cell renderer for severity
    class SeverityRenderer extends DefaultTableCellRenderer {
        public SeverityRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null) {
                String severity = value.toString();
                switch (severity) {
                    case "Critical":
                        c.setBackground(new Color(220, 53, 69)); // Red
                        c.setForeground(Color.WHITE);
                        break;
                    case "High":
                        c.setBackground(new Color(253, 126, 20)); // Orange
                        c.setForeground(Color.WHITE);
                        break;
                    case "Medium":
                        c.setBackground(new Color(255, 193, 7)); // Yellow
                        c.setForeground(Color.BLACK);
                        break;
                    case "Low":
                        c.setBackground(new Color(40, 167, 69)); // Green
                        c.setForeground(Color.WHITE);
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                }
            }
            
            return c;
        }
    }
}