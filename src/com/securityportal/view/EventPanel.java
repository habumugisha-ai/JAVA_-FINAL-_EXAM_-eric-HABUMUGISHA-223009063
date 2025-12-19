// EventPanel.java
package com.securityportal.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.RowFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventPanel extends JPanel {
    private JTable eventTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilter;
    
    public EventPanel() {
        initializeUI();
        loadEventData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Event Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Status:"));
        statusFilter = new JComboBox<>(new String[]{"All", "Open", "Closed", "Investigating", "Resolved", "Pending"});
        statusFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterEvents();
            }
        });
        filterPanel.add(statusFilter);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton resolveBtn = createStyledButton("Resolve Event", new Color(40, 167, 69));
        JButton assignBtn = createStyledButton("Assign Event", new Color(0, 123, 255));
        JButton detailsBtn = createStyledButton("View Details", new Color(108, 117, 125));
        JButton refreshBtn = createStyledButton("Refresh", new Color(23, 162, 184));
        JButton addEventBtn = createStyledButton("Add Event", new Color(111, 66, 193));
        
        buttonPanel.add(resolveBtn);
        buttonPanel.add(assignBtn);
        buttonPanel.add(detailsBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(addEventBtn);

        controlPanel.add(filterPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);

        // Table setup
        String[] columns = {"Event ID", "Description", "Date", "Status", "Priority", "Assigned To", "Location", "Category"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        eventTable = new JTable(tableModel);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventTable.setRowHeight(30);
        eventTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        eventTable.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Set row sorter for filtering
        eventTable.setAutoCreateRowSorter(true);
        
        // Custom renderers for status and priority
        eventTable.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());
        eventTable.getColumnModel().getColumn(4).setCellRenderer(new PriorityRenderer());
        
        JScrollPane scrollPane = new JScrollPane(eventTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Security Events"));
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
        resolveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolveSelectedEvent();
            }
        });
        
        assignBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignSelectedEvent();
            }
        });
        
        detailsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewEventDetails();
            }
        });
        
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshEvents();
            }
        });
        
        addEventBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewEvent();
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
        
        statsPanel.add(createStatCard("Total Events", "24", new Color(0, 123, 255)));
        statsPanel.add(createStatCard("Open", "8", new Color(220, 53, 69)));
        statsPanel.add(createStatCard("In Progress", "6", new Color(255, 193, 7)));
        statsPanel.add(createStatCard("Resolved", "10", new Color(40, 167, 69)));
        statsPanel.add(createStatCard("High Priority", "5", new Color(253, 126, 20)));
        
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
    
    private void loadEventData() {
        Object[][] data = {
            {1, "Unauthorized access attempt at main server room", "2024-01-15 10:30:00", "Open", "High", "John Security", "Server Room", "Security Breach"},
            {2, "Door left open for extended period", "2024-01-15 09:15:00", "Closed", "Medium", "Mike Operator", "Main Entrance", "Access Control"},
            {3, "Motion detected in restricted area after hours", "2024-01-14 16:45:00", "Investigating", "High", "System Admin", "Floor 3 - R&D", "Motion Alert"},
            {4, "Camera offline in parking area", "2024-01-14 14:20:00", "Open", "Medium", "Unassigned", "Parking Lot B1", "Camera Failure"},
            {5, "Multiple failed login attempts", "2024-01-14 11:30:00", "Resolved", "Critical", "Sarah Analyst", "Network", "System Intrusion"},
            {6, "Fire alarm test required", "2024-01-13 15:00:00", "Pending", "Low", "Unassigned", "Building A", "Maintenance"},
            {7, "Suspicious vehicle in parking lot", "2024-01-13 09:45:00", "Investigating", "Medium", "John Security", "Parking Lot", "Security Alert"},
            {8, "Access card cloned alert", "2024-01-12 18:20:00", "Open", "Critical", "Security Team", "Access System", "Security Breach"}
        };
        
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    private void resolveSelectedEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to resolve", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String eventId = tableModel.getValueAt(selectedRow, 0).toString();
        String description = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to resolve this event?\n\n" +
            "Event ID: " + eventId + "\n" +
            "Description: " + description,
            "Confirm Resolution",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            // Update status to Resolved
            tableModel.setValueAt("Resolved", selectedRow, 3);
            
            // Add resolution notes
            String resolutionNotes = JOptionPane.showInputDialog(this,
                "Enter resolution notes:",
                "Resolution Details",
                JOptionPane.QUESTION_MESSAGE);
                
            if (resolutionNotes != null && !resolutionNotes.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Event resolved successfully!\n\n" +
                    "Event ID: " + eventId + "\n" +
                    "Resolution Notes: " + resolutionNotes,
                    "Event Resolved",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Event resolved successfully!",
                    "Event Resolved",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void assignSelectedEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to assign", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String eventId = tableModel.getValueAt(selectedRow, 0).toString();
        String currentAssignee = tableModel.getValueAt(selectedRow, 5).toString();
        
        JDialog assignDialog = new JDialog();
        assignDialog.setTitle("Assign Event #" + eventId);
        assignDialog.setSize(400, 300);
        assignDialog.setLocationRelativeTo(this);
        assignDialog.setLayout(new BorderLayout());
        assignDialog.setModal(true);
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Assignee selection
        JComboBox<String> assigneeCombo = new JComboBox<>(new String[]{
            "Unassigned", "John Security", "Mike Operator", "System Admin", 
            "Sarah Analyst", "Security Team", "IT Support", "Management"
        });
        assigneeCombo.setSelectedItem(currentAssignee);
        formPanel.add(createLabeledField("Assign To:", assigneeCombo));
        
        // Priority selection
        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});
        priorityCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
        formPanel.add(createLabeledField("Priority:", priorityCombo));
        
        // Due date
        JTextField dueDateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        formPanel.add(createLabeledField("Due Date (YYYY-MM-DD):", dueDateField));
        
        // Notes
        JTextArea notesArea = new JTextArea(3, 20);
        notesArea.setLineWrap(true);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        formPanel.add(createLabeledField("Assignment Notes:", notesScroll));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createStyledButton("Assign", new Color(40, 167, 69));
        JButton cancelBtn = createStyledButton("Cancel", new Color(108, 117, 125));
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String assignee = (String) assigneeCombo.getSelectedItem();
                String priority = (String) priorityCombo.getSelectedItem();
                
                tableModel.setValueAt(assignee, selectedRow, 5);
                tableModel.setValueAt(priority, selectedRow, 4);
                
                JOptionPane.showMessageDialog(assignDialog,
                    "Event assigned successfully!\n\n" +
                    "Event ID: " + eventId + "\n" +
                    "Assigned To: " + assignee + "\n" +
                    "Priority: " + priority,
                    "Assignment Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                assignDialog.dispose();
            }
        });
        
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignDialog.dispose();
            }
        });
        
        assignDialog.add(formPanel, BorderLayout.CENTER);
        assignDialog.add(buttonPanel, BorderLayout.SOUTH);
        assignDialog.setVisible(true);
    }
    
    private void viewEventDetails() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to view details", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog detailsDialog = new JDialog();
        detailsDialog.setTitle("Event Details - ID: " + tableModel.getValueAt(selectedRow, 0));
        detailsDialog.setSize(500, 400);
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setLayout(new BorderLayout());
        detailsDialog.setModal(true);
        
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create detail fields (read-only)
        addDetailField(detailsPanel, "Event ID:", tableModel.getValueAt(selectedRow, 0).toString());
        addDetailField(detailsPanel, "Description:", tableModel.getValueAt(selectedRow, 1).toString());
        addDetailField(detailsPanel, "Date/Time:", tableModel.getValueAt(selectedRow, 2).toString());
        addDetailField(detailsPanel, "Status:", tableModel.getValueAt(selectedRow, 3).toString());
        addDetailField(detailsPanel, "Priority:", tableModel.getValueAt(selectedRow, 4).toString());
        addDetailField(detailsPanel, "Assigned To:", tableModel.getValueAt(selectedRow, 5).toString());
        addDetailField(detailsPanel, "Location:", tableModel.getValueAt(selectedRow, 6).toString());
        addDetailField(detailsPanel, "Category:", tableModel.getValueAt(selectedRow, 7).toString());
        
        // Action history (simulated)
        JTextArea historyArea = new JTextArea(5, 30);
        historyArea.setText("2024-01-15 10:30:00 - Event created\n" +
                           "2024-01-15 10:35:00 - Assigned to John Security\n" +
                           "2024-01-15 11:20:00 - Investigation started\n");
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(240, 240, 240));
        JScrollPane historyScroll = new JScrollPane(historyArea);
        
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("Action History"));
        historyPanel.add(historyScroll, BorderLayout.CENTER);
        
        JButton closeBtn = createStyledButton("Close", new Color(108, 117, 125));
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailsDialog.dispose();
            }
        });
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(detailsPanel, BorderLayout.NORTH);
        mainPanel.add(historyPanel, BorderLayout.CENTER);
        
        detailsDialog.add(mainPanel, BorderLayout.CENTER);
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
    
    private JPanel createLabeledField(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }
    
    private void refreshEvents() {
        // Simulate refreshing events
        JOptionPane.showMessageDialog(this, "Events refreshed successfully!", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void addNewEvent() {
        JDialog addDialog = new JDialog();
        addDialog.setTitle("Add New Event");
        addDialog.setSize(400, 500);
        addDialog.setLocationRelativeTo(this);
        addDialog.setLayout(new BorderLayout());
        addDialog.setModal(true);
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField descriptionField = new JTextField();
        formPanel.add(createLabeledField("Description:", descriptionField));
        
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "Security Breach", "Access Control", "Motion Alert", "Camera Failure", 
            "System Intrusion", "Maintenance", "Security Alert"
        });
        formPanel.add(createLabeledField("Category:", categoryCombo));
        
        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});
        formPanel.add(createLabeledField("Priority:", priorityCombo));
        
        JTextField locationField = new JTextField();
        formPanel.add(createLabeledField("Location:", locationField));
        
        JTextArea detailsArea = new JTextArea(3, 20);
        detailsArea.setLineWrap(true);
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        formPanel.add(createLabeledField("Additional Details:", detailsScroll));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createStyledButton("Save Event", new Color(40, 167, 69));
        JButton cancelBtn = createStyledButton("Cancel", new Color(108, 117, 125));
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (descriptionField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(addDialog, "Please enter event description", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Add new event to table
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                Object[] newEvent = {
                    tableModel.getRowCount() + 1,
                    descriptionField.getText(),
                    timestamp,
                    "Open",
                    priorityCombo.getSelectedItem(),
                    "Unassigned",
                    locationField.getText(),
                    categoryCombo.getSelectedItem()
                };
                
                tableModel.addRow(newEvent);
                JOptionPane.showMessageDialog(addDialog, "Event added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
    
    private void filterEvents() {
        String filter = (String) statusFilter.getSelectedItem();
        if ("All".equals(filter)) {
            // Show all rows
            RowFilter<DefaultTableModel, Object> rowFilter = null;
            ((javax.swing.table.TableRowSorter<DefaultTableModel>) eventTable.getRowSorter()).setRowFilter(rowFilter);
        } else {
            // Filter by status
            RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter(filter, 3); // Column 3 is Status
            ((javax.swing.table.TableRowSorter<DefaultTableModel>) eventTable.getRowSorter()).setRowFilter(rowFilter);
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
                    case "Open":
                        c.setBackground(new Color(220, 53, 69)); // Red
                        c.setForeground(Color.WHITE);
                        break;
                    case "Closed":
                        c.setBackground(new Color(40, 167, 69)); // Green
                        c.setForeground(Color.WHITE);
                        break;
                    case "Investigating":
                        c.setBackground(new Color(255, 193, 7)); // Yellow
                        c.setForeground(Color.BLACK);
                        break;
                    case "Resolved":
                        c.setBackground(new Color(23, 162, 184)); // Blue
                        c.setForeground(Color.WHITE);
                        break;
                    case "Pending":
                        c.setBackground(new Color(108, 117, 125)); // Gray
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
    
    // Custom cell renderer for priority
    class PriorityRenderer extends DefaultTableCellRenderer {
        public PriorityRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null) {
                String priority = value.toString();
                switch (priority) {
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