package com.securityportal.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.securityportal.util.ColorScheme;

public class ReportPanel extends JPanel {
    
    public ReportPanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Reporting & Analytics");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.DARK_BG);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Report types panel
        JPanel reportPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        reportPanel.setBackground(ColorScheme.LIGHT_BG);
        reportPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton securityReportBtn = createReportButton("Security Incident Report", ColorScheme.ACCENT_COLOR);
        JButton systemReportBtn = createReportButton("System Activity Report", ColorScheme.PRIMARY_COLOR);
        JButton userReportBtn = createReportButton("User Activity Report", ColorScheme.SUCCESS_COLOR);
        JButton cameraReportBtn = createReportButton("Camera Status Report", ColorScheme.SECONDARY_COLOR);
        JButton alertReportBtn = createReportButton("Alert Summary Report", ColorScheme.WARNING_COLOR);
        JButton customReportBtn = createReportButton("Custom Report", ColorScheme.DARK_BG);
        
        reportPanel.add(securityReportBtn);
        reportPanel.add(systemReportBtn);
        reportPanel.add(userReportBtn);
        reportPanel.add(cameraReportBtn);
        reportPanel.add(alertReportBtn);
        reportPanel.add(customReportBtn);

        // Quick stats panel
        JPanel quickStatsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        quickStatsPanel.setBackground(ColorScheme.LIGHT_BG);
        quickStatsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        quickStatsPanel.add(createQuickStatCard("Reports Generated", "24", ColorScheme.PRIMARY_COLOR));
        quickStatsPanel.add(createQuickStatCard("This Month", "8", ColorScheme.SUCCESS_COLOR));
        quickStatsPanel.add(createQuickStatCard("Pending", "2", ColorScheme.WARNING_COLOR));

        // Add action listeners for all report buttons
        securityReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSecurityIncidentReport();
            }
        });
        
        systemReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSystemActivityReport();
            }
        });

        userReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateUserActivityReport();
            }
        });

        cameraReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateCameraStatusReport();
            }
        });

        alertReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateAlertSummaryReport();
            }
        });

        customReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCustomReportDialog();
            }
        });

        // Create a main content panel to hold everything
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(ColorScheme.LIGHT_BG);
        mainContentPanel.add(quickStatsPanel, BorderLayout.CENTER);
        mainContentPanel.add(reportPanel, BorderLayout.SOUTH);

        // Add components to main panel
        add(titleLabel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
    }
    
    private JButton createReportButton(String text, Color color) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 80));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private JPanel createQuickStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.DARK_GRAY);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }
    
    // Security Incident Report
    private void generateSecurityIncidentReport() {
        final String[] columns = {"Incident ID", "Type", "Severity", "Location", "Date", "Status", "Description"};
        final Object[][] data = {
            {1, "Unauthorized Access", "High", "Main Entrance", "2024-01-15 10:30", "Open", "Attempted breach at front door"},
            {2, "Door Forced", "Medium", "Back Exit", "2024-01-14 22:15", "Closed", "Rear door tampering detected"},
            {3, "Suspicious Activity", "Low", "Parking Lot", "2024-01-13 18:45", "Investigating", "Unknown person loitering"},
            {4, "System Intrusion", "Critical", "Server Room", "2024-01-12 14:20", "Resolved", "Failed login attempts"},
            {5, "Equipment Theft", "High", "Floor 2", "2024-01-10 09:30", "Open", "Missing security equipment"}
        };
        
        final String summary = "Total Incidents: " + data.length + "\n" +
            "Open Cases: 2\n" +
            "Critical Severity: 1\n" +
            "Average Resolution Time: 2.5 days";
            
        showReportDialog("Security Incident Report", columns, data, summary);
    }
    
    // System Activity Report
    private void generateSystemActivityReport() {
        final String[] columns = {"Activity ID", "Component", "Event Type", "Timestamp", "Status", "Duration", "User"};
        final Object[][] data = {
            {101, "Camera System", "Startup", "2024-01-15 06:00", "Success", "45s", "System"},
            {102, "Database", "Backup", "2024-01-15 02:00", "Success", "15m", "System"},
            {103, "Authentication", "Maintenance", "2024-01-14 23:00", "Completed", "30m", "Admin"},
            {104, "Alert System", "Update", "2024-01-14 20:30", "Success", "10m", "System"},
            {105, "Network", "Security Scan", "2024-01-14 15:00", "Running", "2h", "Security1"},
            {106, "Storage", "Cleanup", "2024-01-13 04:00", "Failed", "N/A", "System"}
        };
        
        final String summary = "System Uptime: 99.8%\n" +
            "Failed Activities: 1\n" +
            "Average Response Time: 25 minutes\n" +
            "Last Maintenance: 2024-01-14";
            
        showReportDialog("System Activity Report", columns, data, summary);
    }
    
    // User Activity Report
    private void generateUserActivityReport() {
        final String[] columns = {"User ID", "Username", "Role", "Last Login", "Activities Today", "Status", "Department"};
        final Object[][] data = {
            {1, "admin", "Admin", "2024-01-15 10:30", "15", "Active", "IT"},
            {2, "security1", "Security", "2024-01-15 09:15", "28", "Active", "Security"},
            {3, "operator1", "Operator", "2024-01-14 16:45", "12", "Inactive", "Operations"},
            {4, "manager1", "Manager", "2024-01-13 14:20", "8", "Active", "Management"},
            {5, "auditor1", "Auditor", "2024-01-12 11:00", "5", "Active", "Compliance"}
        };
        
        final String summary = "Total Users: " + data.length + "\n" +
            "Active Users: 4\n" +
            "Average Activities/Day: 13.6\n" +
            "Most Active: security1 (28 activities)";
            
        showReportDialog("User Activity Report", columns, data, summary);
    }
    
    // Camera Status Report
    private void generateCameraStatusReport() {
        final String[] columns = {"Camera ID", "Camera Name", "Location", "Status", "Uptime", "Last Maintenance", "Resolution"};
        final Object[][] data = {
            {1, "Main Entrance", "Building A - Front Door", "Active", "99.9%", "2024-01-10", "4K"},
            {2, "Parking Lot", "Underground Parking B1", "Active", "98.5%", "2024-01-08", "1080p"},
            {3, "Server Room", "Floor 3 - Room 301", "Active", "99.2%", "2024-01-05", "4K"},
            {4, "Back Exit", "Building A - Rear Door", "Inactive", "0%", "2024-01-12", "1080p"},
            {5, "Reception", "Ground Floor", "Maintenance", "95.8%", "2024-01-15", "720p"},
            {6, "Hallway A", "Floor 2 - Corridor", "Active", "99.7%", "2024-01-03", "1080p"}
        };
        
        final String summary = "Total Cameras: " + data.length + "\n" +
            "Active: 4\n" +
            "Inactive: 1\n" +
            "Maintenance: 1\n" +
            "Overall Uptime: 98.5%";
            
        showReportDialog("Camera Status Report", columns, data, summary);
    }
    
    // Alert Summary Report
    private void generateAlertSummaryReport() {
        final String[] columns = {"Alert ID", "Type", "Severity", "Source", "Timestamp", "Status", "Response Time"};
        final Object[][] data = {
            {201, "Temperature", "Critical", "Server Room", "2024-01-15 10:30", "Pending", "N/A"},
            {202, "Camera Offline", "Medium", "Parking Lot", "2024-01-15 09:15", "Acknowledged", "15m"},
            {203, "Access Violation", "High", "Main Entrance", "2024-01-14 16:45", "Resolved", "2h"},
            {204, "System Load", "Low", "Server", "2024-01-14 14:20", "Resolved", "45m"},
            {205, "Network Issue", "Medium", "Network Rack", "2024-01-13 11:00", "Resolved", "1h"},
            {206, "Power Failure", "Critical", "UPS 1", "2024-01-12 08:30", "Resolved", "30m"}
        };
        
        final String summary = "Total Alerts: " + data.length + "\n" +
            "Critical: 2\n" +
            "Pending: 1\n" +
            "Average Response Time: 58 minutes\n" +
            "Resolved: 4";
            
        showReportDialog("Alert Summary Report", columns, data, summary);
    }
    
    // Custom Report Dialog
    private void showCustomReportDialog() {
        final JDialog customDialog = new JDialog();
        customDialog.setTitle("Custom Report Generator");
        customDialog.setSize(500, 400);
        customDialog.setLocationRelativeTo(this);
        customDialog.setLayout(new BorderLayout());
        customDialog.setModal(true);

        // Report options panel
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Date range
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Date Range:"));
        final JComboBox<String> dateRange = new JComboBox<>(new String[]{"Last 7 days", "Last 30 days", "Last 90 days", "Custom Range"});
        datePanel.add(dateRange);
        
        // Report type
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("Report Type:"));
        final JComboBox<String> reportType = new JComboBox<>(new String[]{
            "Security Summary", "System Performance", "User Audit", "Camera Analytics", "Alert Trends"
        });
        typePanel.add(reportType);
        
        // Format
        JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formatPanel.add(new JLabel("Export Format:"));
        final JComboBox<String> exportFormat = new JComboBox<>(new String[]{"PDF", "Excel", "CSV", "HTML", "Print"});
        formatPanel.add(exportFormat);
        
        // Include sections
        JPanel sectionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sectionsPanel.add(new JLabel("Include:"));
        final JCheckBox summaryCheck = new JCheckBox("Summary", true);
        final JCheckBox chartsCheck = new JCheckBox("Charts", true);
        final JCheckBox detailsCheck = new JCheckBox("Detailed Data", false);
        sectionsPanel.add(summaryCheck);
        sectionsPanel.add(chartsCheck);
        sectionsPanel.add(detailsCheck);
        
        optionsPanel.add(new JLabel("Custom Report Configuration:"));
        optionsPanel.add(datePanel);
        optionsPanel.add(typePanel);
        optionsPanel.add(formatPanel);
        optionsPanel.add(sectionsPanel);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton generateBtn = new JButton("Generate Report");
        JButton cancelBtn = new JButton("Cancel");
        
        generateBtn.setBackground(ColorScheme.SUCCESS_COLOR);
        generateBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(ColorScheme.ACCENT_COLOR);
        cancelBtn.setForeground(Color.WHITE);
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(generateBtn);
        
        // Add action listeners
        generateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) reportType.getSelectedItem();
                String selectedFormat = (String) exportFormat.getSelectedItem();
                String selectedDateRange = (String) dateRange.getSelectedItem();

                customDialog.dispose();

                // Generate the custom report based on selections
                generateCustomReport(selectedType, selectedFormat, selectedDateRange,
                    summaryCheck.isSelected(), chartsCheck.isSelected(), detailsCheck.isSelected());
            }
        });
        
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customDialog.dispose();
            }
        });
        
        customDialog.add(optionsPanel, BorderLayout.CENTER);
        customDialog.add(buttonPanel, BorderLayout.SOUTH);
        customDialog.setVisible(true);
    }
    
    // Generate custom report based on user selections
    private void generateCustomReport(String reportType, String format, String dateRange, 
                                    boolean includeSummary, boolean includeCharts, boolean includeDetails) {
        
        // Simulate report data based on type
        String[] columns = {};
        Object[][] data = {};
        String summary = "";
        
        switch (reportType) {
            case "Security Summary":
                columns = new String[]{"Incident Type", "Count", "Avg Resolution", "Trend"};
                data = new Object[][]{
                    {"Unauthorized Access", 12, "2.3 days", "↑"},
                    {"System Intrusion", 5, "1.5 days", "→"},
                    {"Physical Breach", 8, "3.1 days", "↓"},
                    {"Data Theft", 3, "4.2 days", "↑"}
                };
                summary = "Security Summary - " + dateRange + "\nTotal Incidents: 28\nMost Common: Unauthorized Access";
                break;
                
            case "System Performance":
                columns = new String[]{"Component", "Uptime %", "Avg Response", "Status"};
                data = new Object[][]{
                    {"Camera System", "99.2%", "45ms", "Optimal"},
                    {"Database", "99.9%", "12ms", "Optimal"},
                    {"Authentication", "99.5%", "85ms", "Good"},
                    {"Alert System", "98.8%", "120ms", "Good"}
                };
                summary = "System Performance - " + dateRange + "\nOverall Uptime: 99.4%\nAll Systems Operational";
                break;
                
            case "User Audit":
                columns = new String[]{"Department", "Active Users", "Avg Logins", "Compliance %"};
                data = new Object[][]{
                    {"Security", 15, "28/day", "98%"},
                    {"IT", 8, "12/day", "99%"},
                    {"Management", 5, "6/day", "95%"},
                    {"Operations", 22, "18/day", "97%"}
                };
                summary = "User Audit - " + dateRange + "\nTotal Users: 50\nOverall Compliance: 97.5%";
                break;
                
            case "Camera Analytics":
                columns = new String[]{"Location", "Motion Events", "Uptime %", "Storage Used"};
                data = new Object[][]{
                    {"Main Entrance", 245, "99.9%", "45GB"},
                    {"Parking Lot", 189, "98.5%", "32GB"},
                    {"Server Room", 12, "99.2%", "8GB"},
                    {"Hallways", 567, "97.8%", "67GB"}
                };
                summary = "Camera Analytics - " + dateRange + "\nTotal Motion Events: 1,013\nAvg Uptime: 98.9%";
                break;
                
            case "Alert Trends":
                columns = new String[]{"Alert Type", "This Month", "Last Month", "Change %"};
                data = new Object[][]{
                    {"Temperature", 8, 5, "+60%"},
                    {"Camera Offline", 3, 6, "-50%"},
                    {"Access Violation", 12, 10, "+20%"},
                    {"System Load", 5, 7, "-29%"}
                };
                summary = "Alert Trends - " + dateRange + "\nTotal Alerts: 28\nTrend: Stable";
                break;
                
            default:
                columns = new String[]{"Category", "Value", "Status"};
                data = new Object[][]{
                    {"Default", "N/A", "Unknown"}
                };
                summary = "Default Report";
                break;
        }
        
        // Show the custom report
        showCustomReportDialog(reportType + " - Custom", columns, data, summary, format);
    }
    
    // Show custom report in dialog
    private void showCustomReportDialog(String title, String[] columns, Object[][] data, String summary, String format) {
        final String finalTitle = title;
        final String[] finalColumns = columns;
        final Object[][] finalData = data;
        final String finalSummary = summary;
        final String finalFormat = format;
        
        final JDialog reportDialog = new JDialog();
        reportDialog.setTitle(finalTitle);
        reportDialog.setSize(800, 600);
        reportDialog.setLocationRelativeTo(this);
        reportDialog.setLayout(new BorderLayout());
        reportDialog.setModal(true);

        // Create table with data
        DefaultTableModel model = new DefaultTableModel(finalColumns, 0);
        for (Object[] row : finalData) {
            model.addRow(row);
        }
        
        JTable reportTable = new JTable(model);
        reportTable.setRowHeight(25);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 12));
        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane tableScroll = new JScrollPane(reportTable);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        summaryPanel.setBackground(Color.LIGHT_GRAY);
        
        JTextArea summaryArea = new JTextArea(finalSummary);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Arial", Font.BOLD, 12));
        summaryArea.setBackground(Color.LIGHT_GRAY);
        summaryPanel.add(new JLabel("Report Summary:"), BorderLayout.NORTH);
        summaryPanel.add(summaryArea, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportBtn = new JButton("Export to " + finalFormat);
        JButton closeBtn = new JButton("Close");
        
        exportBtn.setBackground(ColorScheme.PRIMARY_COLOR);
        exportBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(ColorScheme.ACCENT_COLOR);
        closeBtn.setForeground(Color.WHITE);
        
        buttonPanel.add(exportBtn);
        buttonPanel.add(closeBtn);
        
        // Add action listeners
        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("PDF".equalsIgnoreCase(finalFormat)) {
                    exportReportToPDF(finalTitle, finalColumns, finalData, finalSummary);
                } else if ("Print".equalsIgnoreCase(finalFormat)) {
                    printReport(finalTitle, finalColumns, finalData, finalSummary);
                } else {
                    // Handle other formats (Excel, CSV, HTML)
                    exportReportToOtherFormat(finalTitle, finalColumns, finalData, finalSummary, finalFormat);
                }
                reportDialog.dispose();
            }
        });
        
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reportDialog.dispose();
            }
        });
        
        // Add components to dialog
        reportDialog.add(summaryPanel, BorderLayout.NORTH);
        reportDialog.add(tableScroll, BorderLayout.CENTER);
        reportDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        reportDialog.setVisible(true);
    }
    
    // Generic method to show report in dialog
    private void showReportDialog(final String title, final String[] columns, final Object[][] data, final String summary) {
        final JDialog reportDialog = new JDialog();
        reportDialog.setTitle(title);
        reportDialog.setSize(800, 600);
        reportDialog.setLocationRelativeTo(this);
        reportDialog.setLayout(new BorderLayout());
        reportDialog.setModal(true);

        // Create table with data
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Object[] row : data) {
            model.addRow(row);
        }
        
        JTable reportTable = new JTable(model);
        reportTable.setRowHeight(25);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 12));
        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane tableScroll = new JScrollPane(reportTable);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        summaryPanel.setBackground(Color.LIGHT_GRAY);
        
        JTextArea summaryArea = new JTextArea(summary);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Arial", Font.BOLD, 12));
        summaryArea.setBackground(Color.LIGHT_GRAY);
        summaryPanel.add(new JLabel("Report Summary:"), BorderLayout.NORTH);
        summaryPanel.add(summaryArea, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportBtn = new JButton("Export to PDF");
        JButton printBtn = new JButton("Print");
        JButton closeBtn = new JButton("Close");
        
        exportBtn.setBackground(ColorScheme.PRIMARY_COLOR);
        exportBtn.setForeground(Color.WHITE);
        printBtn.setBackground(ColorScheme.SECONDARY_COLOR);
        printBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(ColorScheme.ACCENT_COLOR);
        closeBtn.setForeground(Color.WHITE);
        
        buttonPanel.add(exportBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(closeBtn);
        
        // Add action listeners
        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportReportToPDF(title, columns, data, summary);
                reportDialog.dispose();
            }
        });
        
        printBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printReport(title, columns, data, summary);
                reportDialog.dispose();
            }
        });
        
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reportDialog.dispose();
            }
        });
        
        // Add components to dialog
        reportDialog.add(summaryPanel, BorderLayout.NORTH);
        reportDialog.add(tableScroll, BorderLayout.CENTER);
        reportDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        reportDialog.setVisible(true);
    }
    
    // Enhanced PDF Export functionality
    private void exportReportToPDF(String title, String[] columns, Object[][] data, String summary) {
        try {
            // Create reports directory if it doesn't exist
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }
            
            // Use JFileChooser to let user choose save location
            JFileChooser fileChooser = new JFileChooser(reportsDir);
            fileChooser.setDialogTitle("Save PDF Report");
            fileChooser.setSelectedFile(new File(title.replace(" ", "_") + ".pdf"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf"));
            
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                
                // Ensure .pdf extension
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                    fileToSave = new File(filePath);
                }
                
                // Create the PDF content (simulated - in real implementation, use iText or similar)
                createPDFFile(fileToSave, title, columns, data, summary);
                
                JOptionPane.showMessageDialog(this,
                    "PDF Report exported successfully!\n\n" +
                    "File: " + fileToSave.getName() + "\n" +
                    "Location: " + fileToSave.getAbsolutePath() + "\n" +
                    "Rows: " + data.length + "\n" +
                    "Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Ask if user wants to open the file
                int option = JOptionPane.showConfirmDialog(this,
                    "Open the exported PDF file?",
                    "Open File",
                    JOptionPane.YES_NO_OPTION);
                
                if (option == JOptionPane.YES_OPTION) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(fileToSave);
                    }
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error exporting PDF: " + e.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // Create actual PDF file (simplified version)
    private void createPDFFile(File file, String title, String[] columns, Object[][] data, String summary) {
        try {
            // In a real implementation, you would use iText or similar library
            // For now, we'll create a simple text file to simulate
            StringBuilder content = new StringBuilder();
            content.append("====================================\n");
            content.append(title.toUpperCase()).append("\n");
            content.append("====================================\n\n");
            content.append("Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n\n");
            content.append("SUMMARY:\n");
            content.append(summary).append("\n\n");
            content.append("DETAILED DATA:\n\n");
            
            // Add headers
            for (int i = 0; i < columns.length; i++) {
                content.append(String.format("%-20s", columns[i]));
            }
            content.append("\n");
            
            // Add separator
            for (int i = 0; i < columns.length; i++) {
                content.append("--------------------");
            }
            content.append("\n");
            
            // Add data rows
            for (Object[] row : data) {
                for (Object cell : row) {
                    content.append(String.format("%-20s", cell.toString()));
                }
                content.append("\n");
            }
            
            content.append("\n====================================\n");
            content.append("End of Report\n");
            content.append("====================================\n");
            
            // Write to file
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
                fos.write(content.toString().getBytes());
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create PDF file: " + e.getMessage(), e);
        }
    }
    
    // Print report functionality
    private void printReport(String title, String[] columns, Object[][] data, String summary) {
        try {
            // Create a printable component
            JTextArea printArea = new JTextArea();
            printArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
            
            StringBuilder content = new StringBuilder();
            content.append("\n\n");
            content.append("====================================\n");
            content.append(title.toUpperCase()).append("\n");
            content.append("====================================\n\n");
            content.append("Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n\n");
            content.append("SUMMARY:\n");
            content.append(summary).append("\n\n");
            content.append("DETAILED DATA:\n\n");
            
            // Format table for printing
            int[] colWidths = new int[columns.length];
            for (int i = 0; i < columns.length; i++) {
                colWidths[i] = Math.max(columns[i].length(), 15);
            }
            
            // Calculate row widths
            for (Object[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    String cell = row[i] != null ? row[i].toString() : "";
                    colWidths[i] = Math.max(colWidths[i], Math.min(cell.length(), 30));
                }
            }
            
            // Add headers
            for (int i = 0; i < columns.length; i++) {
                content.append(String.format("%-" + colWidths[i] + "s", 
                    columns[i].length() > colWidths[i] ? 
                    columns[i].substring(0, colWidths[i]) : columns[i]));
                content.append("  ");
            }
            content.append("\n");
            
            // Add separator
            for (int i = 0; i < columns.length; i++) {
                content.append("-".repeat(colWidths[i]));
                content.append("  ");
            }
            content.append("\n");
            
            // Add data rows
            for (Object[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    String cell = row[i] != null ? row[i].toString() : "";
                    content.append(String.format("%-" + colWidths[i] + "s", 
                        cell.length() > colWidths[i] ? 
                        cell.substring(0, colWidths[i]) : cell));
                    content.append("  ");
                }
                content.append("\n");
            }
            
            content.append("\n====================================\n");
            content.append("End of Report\n");
            content.append("====================================\n");
            
            printArea.setText(content.toString());
            
            // Show print dialog
            boolean complete = printArea.print();
            
            if (complete) {
                JOptionPane.showMessageDialog(this,
                    "Report sent to printer successfully!",
                    "Print Complete",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Printing was cancelled.",
                    "Print Cancelled",
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error printing report: " + e.getMessage(),
                "Print Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Handle other export formats (Excel, CSV, HTML)
    private void exportReportToOtherFormat(String title, String[] columns, Object[][] data, String summary, String format) {
        try {
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }
            
            String extension;
            String formatName;
            
            switch (format.toLowerCase()) {
                case "excel":
                    extension = "xls";
                    formatName = "Excel";
                    break;
                case "csv":
                    extension = "csv";
                    formatName = "CSV";
                    break;
                case "html":
                    extension = "html";
                    formatName = "HTML";
                    break;
                default:
                    extension = "txt";
                    formatName = "Text";
            }
            
            JFileChooser fileChooser = new JFileChooser(reportsDir);
            fileChooser.setDialogTitle("Save " + formatName + " Report");
            fileChooser.setSelectedFile(new File(title.replace(" ", "_") + "." + extension));
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                formatName + " Files (*." + extension + ")", extension));
            
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                
                // Ensure correct extension
                if (!filePath.toLowerCase().endsWith("." + extension)) {
                    filePath += "." + extension;
                    fileToSave = new File(filePath);
                }
                
                // Create file content based on format
                switch (format.toLowerCase()) {
                    case "csv":
                        exportToCSV(fileToSave, title, columns, data, summary);
                        break;
                    case "html":
                        exportToHTML(fileToSave, title, columns, data, summary);
                        break;
                    case "excel":
                        exportToExcel(fileToSave, title, columns, data, summary);
                        break;
                    default:
                        exportToText(fileToSave, title, columns, data, summary);
                }
                
                JOptionPane.showMessageDialog(this,
                    formatName + " Report exported successfully!\n\n" +
                    "File: " + fileToSave.getName() + "\n" +
                    "Location: " + fileToSave.getAbsolutePath() + "\n" +
                    "Rows: " + data.length + "\n" +
                    "Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error exporting " + format + " report: " + e.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportToCSV(File file, String title, String[] columns, Object[][] data, String summary) {
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
            StringBuilder content = new StringBuilder();
            
            // Add title and summary as comments
            content.append("# ").append(title).append("\n");
            content.append("# Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
            
            String[] summaryLines = summary.split("\n");
            for (String line : summaryLines) {
                content.append("# ").append(line).append("\n");
            }
            content.append("\n");
            
            // Add headers
            for (int i = 0; i < columns.length; i++) {
                content.append("\"").append(columns[i]).append("\"");
                if (i < columns.length - 1) {
                    content.append(",");
                }
            }
            content.append("\n");
            
            // Add data rows
            for (Object[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    String cell = row[i] != null ? row[i].toString() : "";
                    // Escape quotes and commas
                    cell = cell.replace("\"", "\"\"");
                    content.append("\"").append(cell).append("\"");
                    if (i < row.length - 1) {
                        content.append(",");
                    }
                }
                content.append("\n");
            }
            
            fos.write(content.toString().getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create CSV file: " + e.getMessage(), e);
        }
    }
    
    private void exportToHTML(File file, String title, String[] columns, Object[][] data, String summary) {
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
            StringBuilder content = new StringBuilder();
            
            content.append("<!DOCTYPE html>\n");
            content.append("<html>\n");
            content.append("<head>\n");
            content.append("<title>").append(title).append("</title>\n");
            content.append("<style>\n");
            content.append("body { font-family: Arial, sans-serif; margin: 40px; }\n");
            content.append("h1 { color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }\n");
            content.append(".summary { background-color: #f5f5f5; padding: 15px; border-radius: 5px; margin-bottom: 20px; }\n");
            content.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
            content.append("th { background-color: #4CAF50; color: white; padding: 12px; text-align: left; }\n");
            content.append("td { padding: 10px; border-bottom: 1px solid #ddd; }\n");
            content.append("tr:nth-child(even) { background-color: #f2f2f2; }\n");
            content.append(".footer { margin-top: 30px; font-size: 12px; color: #666; }\n");
            content.append("</style>\n");
            content.append("</head>\n");
            content.append("<body>\n");
            content.append("<h1>").append(title).append("</h1>\n");
            content.append("<div class=\"summary\">\n");
            content.append("<strong>Generated: </strong>").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("<br>\n");
            content.append("<strong>Summary: </strong><br>\n");
            content.append(summary.replace("\n", "<br>\n"));
            content.append("</div>\n");
            
            content.append("<table>\n");
            content.append("<tr>\n");
            for (String column : columns) {
                content.append("<th>").append(column).append("</th>\n");
            }
            content.append("</tr>\n");
            
            for (Object[] row : data) {
                content.append("<tr>\n");
                for (Object cell : row) {
                    content.append("<td>").append(cell != null ? cell.toString() : "").append("</td>\n");
                }
                content.append("</tr>\n");
            }
            
            content.append("</table>\n");
            content.append("<div class=\"footer\">\n");
            content.append("Report generated by Security Portal System<br>\n");
            content.append("Total records: ").append(data.length).append("\n");
            content.append("</div>\n");
            content.append("</body>\n");
            content.append("</html>");
            
            fos.write(content.toString().getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create HTML file: " + e.getMessage(), e);
        }
    }
    
    private void exportToExcel(File file, String title, String[] columns, Object[][] data, String summary) {
        // For Excel, we'll create a CSV with .xls extension
        // In a real implementation, you might use Apache POI or similar library
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
            StringBuilder content = new StringBuilder();
            
            // Excel often can read simple CSV/TSV with .xls extension
            // Add title and summary
            content.append("Report Title: ").append(title).append("\n");
            content.append("Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
            content.append("Summary:\n");
            content.append(summary).append("\n\n");
            
            // Add headers
            for (int i = 0; i < columns.length; i++) {
                content.append(columns[i]);
                if (i < columns.length - 1) {
                    content.append("\t"); // Tab separator for Excel
                }
            }
            content.append("\n");
            
            // Add data rows
            for (Object[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    String cell = row[i] != null ? row[i].toString() : "";
                    content.append(cell);
                    if (i < row.length - 1) {
                        content.append("\t");
                    }
                }
                content.append("\n");
            }
            
            fos.write(content.toString().getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Excel file: " + e.getMessage(), e);
        }
    }
    
    private void exportToText(File file, String title, String[] columns, Object[][] data, String summary) {
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
            StringBuilder content = new StringBuilder();
            
            content.append("=".repeat(50)).append("\n");
            content.append(title.toUpperCase()).append("\n");
            content.append("=".repeat(50)).append("\n\n");
            content.append("Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n\n");
            content.append("SUMMARY:\n");
            content.append(summary).append("\n\n");
            content.append("DETAILED DATA:\n\n");
            
            // Calculate column widths
            int[] colWidths = new int[columns.length];
            for (int i = 0; i < columns.length; i++) {
                colWidths[i] = Math.max(columns[i].length(), 15);
            }
            
            for (Object[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    String cell = row[i] != null ? row[i].toString() : "";
                    colWidths[i] = Math.max(colWidths[i], Math.min(cell.length(), 30));
                }
            }
            
            // Add headers
            for (int i = 0; i < columns.length; i++) {
                content.append(String.format("%-" + colWidths[i] + "s", 
                    columns[i].length() > colWidths[i] ? 
                    columns[i].substring(0, colWidths[i]) : columns[i]));
                content.append("  ");
            }
            content.append("\n");
            
            // Add separator
            for (int i = 0; i < columns.length; i++) {
                content.append("-".repeat(colWidths[i]));
                content.append("  ");
            }
            content.append("\n");
            
            // Add data rows
            for (Object[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    String cell = row[i] != null ? row[i].toString() : "";
                    content.append(String.format("%-" + colWidths[i] + "s", 
                        cell.length() > colWidths[i] ? 
                        cell.substring(0, colWidths[i]) : cell));
                    content.append("  ");
                }
                content.append("\n");
            }
            
            content.append("\n").append("=".repeat(50)).append("\n");
            content.append("End of Report - Total Records: ").append(data.length).append("\n");
            content.append("=".repeat(50)).append("\n");
            
            fos.write(content.toString().getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create text file: " + e.getMessage(), e);
        }
    }
}