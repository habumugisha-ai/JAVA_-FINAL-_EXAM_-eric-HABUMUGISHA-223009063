// AccessControlPanel.java
package com.securityportal.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccessControlPanel extends JPanel {
    private JTable accessTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> accessLevelFilter;
    private int nextAccessId = 9; // Updated to continue from existing data
    
    public AccessControlPanel() {
        initializeUI();
        loadAccessData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Access Control Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Access Level:"));
        accessLevelFilter = new JComboBox<>(new String[]{"All", "Full Access", "Limited Access", "Restricted Access", "Temporary Access"});
        accessLevelFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterAccess();
            }
        });
        filterPanel.add(accessLevelFilter);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton grantBtn = createStyledButton("Grant Access", new Color(40, 167, 69));
        JButton revokeBtn = createStyledButton("Revoke Access", new Color(220, 53, 69));
        JButton modifyBtn = createStyledButton("Modify Permissions", new Color(0, 123, 255));
        JButton refreshBtn = createStyledButton("Refresh", new Color(23, 162, 184));
        JButton viewDetailsBtn = createStyledButton("View Details", new Color(111, 66, 193));
        
        buttonPanel.add(grantBtn);
        buttonPanel.add(revokeBtn);
        buttonPanel.add(modifyBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewDetailsBtn);

        controlPanel.add(filterPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);

        // Table setup
        String[] columns = {"Access ID", "User/Role", "Access Level", "Permissions", "Valid Until", "Status", "Last Modified"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        accessTable = new JTable(tableModel);
        accessTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accessTable.setRowHeight(30);
        accessTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        accessTable.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Set row sorter for filtering
        accessTable.setAutoCreateRowSorter(true);
        
        // Custom renderers for status and access level
        accessTable.getColumnModel().getColumn(2).setCellRenderer(new AccessLevelRenderer());
        accessTable.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());
        
        JScrollPane scrollPane = new JScrollPane(accessTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Access Control List"));
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
        grantBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grantAccess();
            }
        });
        
        revokeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                revokeAccess();
            }
        });
        
        modifyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyPermissions();
            }
        });
        
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAccess();
            }
        });
        
        viewDetailsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAccessDetails();
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
        
        statsPanel.add(createStatCard("Total Users", "24", new Color(0, 123, 255)));
        statsPanel.add(createStatCard("Active Access", "18", new Color(40, 167, 69)));
        statsPanel.add(createStatCard("Expired", "3", new Color(220, 53, 69)));
        statsPanel.add(createStatCard("Full Access", "2", new Color(111, 66, 193)));
        statsPanel.add(createStatCard("Temporary", "5", new Color(255, 193, 7)));
        
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
    
    private void loadAccessData() {
        Object[][] data = {
            {1, "admin", "Full Access", "All permissions", "Permanent", "Active", "2024-01-15"},
            {2, "security1", "Limited Access", "View cameras, Manage events", "2024-12-31", "Active", "2024-01-10"},
            {3, "operator1", "Restricted Access", "View only", "2024-06-30", "Active", "2024-01-08"},
            {4, "auditor1", "Read Only", "View reports, Audit logs", "2024-03-31", "Active", "2024-01-12"},
            {5, "manager1", "Management Access", "View all, Generate reports", "Permanent", "Active", "2024-01-05"},
            {6, "guest1", "Temporary Access", "Limited view", "2024-01-20", "Expired", "2024-01-01"},
            {7, "support1", "Support Access", "Troubleshoot systems", "2024-02-28", "Active", "2024-01-14"},
            {8, "backup1", "Backup Access", "Data backup only", "2024-04-15", "Active", "2024-01-09"}
        };
        
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    private void grantAccess() {
        final JDialog grantDialog = new JDialog();
        grantDialog.setTitle("Grant New Access");
        grantDialog.setSize(500, 500);
        grantDialog.setLocationRelativeTo(this);
        grantDialog.setLayout(new BorderLayout());
        grantDialog.setModal(true);
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // User/Role selection
        final JComboBox<String> userCombo = new JComboBox<>(new String[]{
            "Select User...", "admin", "security1", "security2", "operator1", "operator2",
            "auditor1", "manager1", "manager2", "support1", "support2", "backup1"
        });
        formPanel.add(createLabeledField("User/Role:", userCombo));
        
        // Access Level
        final JComboBox<String> accessLevelCombo = new JComboBox<>(new String[]{
            "Full Access", "Limited Access", "Restricted Access", "Read Only", 
            "Management Access", "Support Access", "Temporary Access", "Backup Access"
        });
        formPanel.add(createLabeledField("Access Level:", accessLevelCombo));
        
        // Permissions selection
        JPanel permissionsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        final JCheckBox viewCamerasCheck = new JCheckBox("View Cameras");
        final JCheckBox manageEventsCheck = new JCheckBox("Manage Events");
        final JCheckBox viewReportsCheck = new JCheckBox("View Reports");
        final JCheckBox generateReportsCheck = new JCheckBox("Generate Reports");
        final JCheckBox manageUsersCheck = new JCheckBox("Manage Users");
        final JCheckBox systemConfigCheck = new JCheckBox("System Configuration");
        final JCheckBox auditLogsCheck = new JCheckBox("Audit Logs");
        final JCheckBox backupDataCheck = new JCheckBox("Backup Data");
        
        permissionsPanel.add(viewCamerasCheck);
        permissionsPanel.add(manageEventsCheck);
        permissionsPanel.add(viewReportsCheck);
        permissionsPanel.add(generateReportsCheck);
        permissionsPanel.add(manageUsersCheck);
        permissionsPanel.add(systemConfigCheck);
        permissionsPanel.add(auditLogsCheck);
        permissionsPanel.add(backupDataCheck);
        
        formPanel.add(createLabeledField("Permissions:", permissionsPanel));
        
        // Access Duration
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup durationGroup = new ButtonGroup();
        final JRadioButton permanentRadio = new JRadioButton("Permanent", true);
        final JRadioButton temporaryRadio = new JRadioButton("Temporary");
        durationGroup.add(permanentRadio);
        durationGroup.add(temporaryRadio);
        
        final JTextField expiryField = new JTextField(10);
        expiryField.setText(new SimpleDateFormat("yyyy-MM-dd").format(getNextMonthDate()));
        expiryField.setEnabled(false);
        
        temporaryRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                expiryField.setEnabled(temporaryRadio.isSelected());
            }
        });
        
        durationPanel.add(permanentRadio);
        durationPanel.add(temporaryRadio);
        durationPanel.add(new JLabel("Valid Until:"));
        durationPanel.add(expiryField);
        
        formPanel.add(createLabeledField("Access Duration:", durationPanel));
        
        // Reason for access
        JTextArea reasonArea = new JTextArea(3, 20);
        reasonArea.setLineWrap(true);
        JScrollPane reasonScroll = new JScrollPane(reasonArea);
        formPanel.add(createLabeledField("Reason for Access:", reasonScroll));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createStyledButton("Grant Access", new Color(40, 167, 69));
        JButton cancelBtn = createStyledButton("Cancel", new Color(108, 117, 125));
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userCombo.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(grantDialog, "Please select a user/role", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Build permissions string
                StringBuilder permissions = new StringBuilder();
                if (viewCamerasCheck.isSelected()) permissions.append("View Cameras, ");
                if (manageEventsCheck.isSelected()) permissions.append("Manage Events, ");
                if (viewReportsCheck.isSelected()) permissions.append("View Reports, ");
                if (generateReportsCheck.isSelected()) permissions.append("Generate Reports, ");
                if (manageUsersCheck.isSelected()) permissions.append("Manage Users, ");
                if (systemConfigCheck.isSelected()) permissions.append("System Configuration, ");
                if (auditLogsCheck.isSelected()) permissions.append("Audit Logs, ");
                if (backupDataCheck.isSelected()) permissions.append("Backup Data, ");
                
                if (permissions.length() > 0) {
                    permissions.setLength(permissions.length() - 2); // Remove last comma
                } else {
                    permissions.append("No permissions");
                }
                
                // Determine valid until date
                String validUntil = permanentRadio.isSelected() ? "Permanent" : expiryField.getText();
                
                // Add new access to table
                String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                Object[] newAccess = {
                    nextAccessId++,
                    userCombo.getSelectedItem(),
                    accessLevelCombo.getSelectedItem(),
                    permissions.toString(),
                    validUntil,
                    "Active",
                    currentDate
                };
                
                tableModel.addRow(newAccess);
                
                JOptionPane.showMessageDialog(grantDialog,
                    "Access granted successfully!\n\n" +
                    "User: " + userCombo.getSelectedItem() + "\n" +
                    "Access Level: " + accessLevelCombo.getSelectedItem() + "\n" +
                    "Valid Until: " + validUntil,
                    "Access Granted",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                grantDialog.dispose();
            }
        });
        
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grantDialog.dispose();
            }
        });
        
        grantDialog.add(formPanel, BorderLayout.CENTER);
        grantDialog.add(buttonPanel, BorderLayout.SOUTH);
        grantDialog.setVisible(true);
    }
    
    private void revokeAccess() {
        int selectedRow = accessTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an access entry to revoke", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row index to model row index
        int modelRow = accessTable.convertRowIndexToModel(selectedRow);
        
        String accessId = tableModel.getValueAt(modelRow, 0).toString();
        String userRole = tableModel.getValueAt(modelRow, 1).toString();
        String accessLevel = tableModel.getValueAt(modelRow, 2).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to revoke access?\n\n" +
            "Access ID: " + accessId + "\n" +
            "User/Role: " + userRole + "\n" +
            "Access Level: " + accessLevel + "\n\n" +
            "This action cannot be undone!",
            "Confirm Revocation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            // Remove the access entry
            tableModel.removeRow(modelRow);
            
            JOptionPane.showMessageDialog(this,
                "Access revoked successfully!\n\n" +
                "User: " + userRole + "\n" +
                "Access Level: " + accessLevel + "\n" +
                "All permissions have been removed.",
                "Access Revoked",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void modifyPermissions() {
        int selectedRow = accessTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an access entry to modify", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row index to model row index
        int modelRow = accessTable.convertRowIndexToModel(selectedRow);
        
        String accessId = tableModel.getValueAt(modelRow, 0).toString();
        final String currentUser = tableModel.getValueAt(modelRow, 1).toString();
        String currentAccessLevel = tableModel.getValueAt(modelRow, 2).toString();
        String currentPermissions = tableModel.getValueAt(modelRow, 3).toString();
        String currentValidUntil = tableModel.getValueAt(modelRow, 4).toString();
        
        final JDialog modifyDialog = new JDialog();
        modifyDialog.setTitle("Modify Permissions - " + currentUser);
        modifyDialog.setSize(500, 400);
        modifyDialog.setLocationRelativeTo(this);
        modifyDialog.setLayout(new BorderLayout());
        modifyDialog.setModal(true);
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Access Level
        final JComboBox<String> accessLevelCombo = new JComboBox<>(new String[]{
            "Full Access", "Limited Access", "Restricted Access", "Read Only", 
            "Management Access", "Support Access", "Temporary Access", "Backup Access"
        });
        accessLevelCombo.setSelectedItem(currentAccessLevel);
        formPanel.add(createLabeledField("Access Level:", accessLevelCombo));
        
        // Permissions (text field for simplicity)
        final JTextArea permissionsArea = new JTextArea(currentPermissions, 3, 20);
        permissionsArea.setLineWrap(true);
        JScrollPane permissionsScroll = new JScrollPane(permissionsArea);
        formPanel.add(createLabeledField("Permissions:", permissionsScroll));
        
        // Valid Until
        final JTextField validUntilField = new JTextField(currentValidUntil);
        formPanel.add(createLabeledField("Valid Until:", validUntilField));
        
        // Modification reason
        JTextArea reasonArea = new JTextArea(3, 20);
        reasonArea.setLineWrap(true);
        JScrollPane reasonScroll = new JScrollPane(reasonArea);
        formPanel.add(createLabeledField("Modification Reason:", reasonScroll));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createStyledButton("Save Changes", new Color(40, 167, 69));
        JButton cancelBtn = createStyledButton("Cancel", new Color(108, 117, 125));
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (permissionsArea.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(modifyDialog, "Please enter permissions", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (validUntilField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(modifyDialog, "Please enter valid until date", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Update the access entry
                tableModel.setValueAt(accessLevelCombo.getSelectedItem(), modelRow, 2);
                tableModel.setValueAt(permissionsArea.getText(), modelRow, 3);
                tableModel.setValueAt(validUntilField.getText(), modelRow, 4);
                tableModel.setValueAt(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), modelRow, 6);
                
                JOptionPane.showMessageDialog(modifyDialog,
                    "Permissions modified successfully!\n\n" +
                    "User: " + currentUser + "\n" +
                    "New Access Level: " + accessLevelCombo.getSelectedItem() + "\n" +
                    "Changes have been applied immediately.",
                    "Permissions Modified",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                modifyDialog.dispose();
            }
        });
        
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyDialog.dispose();
            }
        });
        
        modifyDialog.add(formPanel, BorderLayout.CENTER);
        modifyDialog.add(buttonPanel, BorderLayout.SOUTH);
        modifyDialog.setVisible(true);
    }
    
    private JPanel createLabeledField(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }
    
    private void refreshAccess() {
        // Simulate refreshing access data
        JOptionPane.showMessageDialog(this, "Access control data refreshed successfully!", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void viewAccessDetails() {
        int selectedRow = accessTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an access entry to view details", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row index to model row index
        int modelRow = accessTable.convertRowIndexToModel(selectedRow);
        
        JDialog detailsDialog = new JDialog();
        detailsDialog.setTitle("Access Details - ID: " + tableModel.getValueAt(modelRow, 0));
        detailsDialog.setSize(500, 400);
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setLayout(new BorderLayout());
        detailsDialog.setModal(true);
        
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create detail fields (read-only)
        addDetailField(detailsPanel, "Access ID:", tableModel.getValueAt(modelRow, 0).toString());
        addDetailField(detailsPanel, "User/Role:", tableModel.getValueAt(modelRow, 1).toString());
        addDetailField(detailsPanel, "Access Level:", tableModel.getValueAt(modelRow, 2).toString());
        addDetailField(detailsPanel, "Permissions:", tableModel.getValueAt(modelRow, 3).toString());
        addDetailField(detailsPanel, "Valid Until:", tableModel.getValueAt(modelRow, 4).toString());
        addDetailField(detailsPanel, "Status:", tableModel.getValueAt(modelRow, 5).toString());
        addDetailField(detailsPanel, "Last Modified:", tableModel.getValueAt(modelRow, 6).toString());
        
        JButton closeBtn = createStyledButton("Close", new Color(108, 117, 125));
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailsDialog.dispose();
            }
        });
        
        detailsDialog.add(detailsPanel, BorderLayout.CENTER);
        detailsDialog.add(closeBtn, BorderLayout.SOUTH);
        detailsDialog.setVisible(true);
    }
    
    private void addDetailField(JPanel panel, String label, String value) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.BOLD, 12));
        
        JTextField valueField = new JTextField(value);
        valueField.setEditable(false);
        valueField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        valueField.setBackground(new Color(240, 240, 240));
        
        fieldPanel.add(labelComp, BorderLayout.WEST);
        fieldPanel.add(valueField, BorderLayout.CENTER);
        panel.add(fieldPanel);
    }
    
    private void filterAccess() {
        String filter = (String) accessLevelFilter.getSelectedItem();
        // Simple filtering notification
        if ("All".equals(filter)) {
            // Show all access entries
        } else {
            JOptionPane.showMessageDialog(this, 
                "Filtering access by: " + filter,
                "Filter Applied",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private Date getNextMonthDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }
    
    // Custom cell renderer for access level
    class AccessLevelRenderer extends DefaultTableCellRenderer {
        public AccessLevelRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null) {
                String accessLevel = value.toString();
                switch (accessLevel) {
                    case "Full Access":
                        c.setBackground(new Color(220, 53, 69)); // Red
                        c.setForeground(Color.WHITE);
                        break;
                    case "Limited Access":
                        c.setBackground(new Color(253, 126, 20)); // Orange
                        c.setForeground(Color.WHITE);
                        break;
                    case "Restricted Access":
                        c.setBackground(new Color(255, 193, 7)); // Yellow
                        c.setForeground(Color.BLACK);
                        break;
                    case "Read Only":
                        c.setBackground(new Color(40, 167, 69)); // Green
                        c.setForeground(Color.WHITE);
                        break;
                    case "Management Access":
                        c.setBackground(new Color(111, 66, 193)); // Purple
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
                    case "Active":
                        c.setBackground(new Color(40, 167, 69)); // Green
                        c.setForeground(Color.WHITE);
                        break;
                    case "Expired":
                        c.setBackground(new Color(108, 117, 125)); // Gray
                        c.setForeground(Color.WHITE);
                        break;
                    case "Suspended":
                        c.setBackground(new Color(255, 193, 7)); // Yellow
                        c.setForeground(Color.BLACK);
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