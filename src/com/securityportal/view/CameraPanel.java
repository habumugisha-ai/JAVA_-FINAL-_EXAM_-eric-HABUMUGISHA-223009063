// CameraPanel.java - Complete Fixed Version
package com.securityportal.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class CameraPanel extends JPanel {
    private JTable cameraTable;
    private DefaultTableModel tableModel;
    private int nextCameraId = 9;
    
    // For snapshot functionality
    private BufferedImage currentSnapshot;
    
    public CameraPanel() {
        initializeUI();
        loadCameraData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Camera Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Control buttons panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JButton addBtn = createStyledButton("Add Camera", new Color(40, 167, 69));
        JButton editBtn = createStyledButton("Edit Camera", new Color(0, 123, 255));
        JButton deleteBtn = createStyledButton("Delete Camera", new Color(220, 53, 69));
        JButton viewFeedBtn = createStyledButton("View Live Feed", new Color(108, 117, 125));
        JButton viewAllBtn = createStyledButton("View All Active", new Color(111, 66, 193));
        JButton refreshBtn = createStyledButton("Refresh", new Color(23, 162, 184));
        
        controlPanel.add(addBtn);
        controlPanel.add(editBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(viewFeedBtn);
        controlPanel.add(viewAllBtn);
        controlPanel.add(refreshBtn);

        // Table setup
        String[] columns = {"Camera ID", "Camera Name", "Location", "Status", "Last Active", "Resolution", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cameraTable = new JTable(tableModel);
        cameraTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cameraTable.setRowHeight(35);
        cameraTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        cameraTable.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Center align ID column and status column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(JLabel.CENTER);
            }
        };
        cameraTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        cameraTable.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());
        
        JScrollPane scrollPane = new JScrollPane(cameraTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Camera List"));
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
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddCameraDialog();
            }
        });
        
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditCameraDialog();
            }
        });
        
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedCamera();
            }
        });
        
        viewFeedBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewLiveFeed();
            }
        });
        
        viewAllBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllActiveCameras();
            }
        });
        
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshCameraData();
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
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        statsPanel.add(createStatCard("Total Cameras", "8", new Color(0, 123, 255)));
        statsPanel.add(createStatCard("Active", "6", new Color(40, 167, 69)));
        statsPanel.add(createStatCard("Inactive", "2", new Color(220, 53, 69)));
        statsPanel.add(createStatCard("Uptime", "98.5%", new Color(255, 193, 7)));
        
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
    
    private void loadCameraData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Sample camera data with additional details
        Object[][] data = {
            {1, "Main Entrance", "Building A - Front Door", "Active", "2024-01-15 10:30:00", "4K", "PTZ"},
            {2, "Parking Lot", "Underground Parking B1", "Active", "2024-01-15 09:15:00", "1080p", "Fixed"},
            {3, "Server Room", "Floor 3 - Room 301", "Active", "2024-01-15 11:20:00", "4K", "Fixed"},
            {4, "Back Exit", "Building A - Rear Door", "Inactive", "2024-01-14 16:45:00", "1080p", "Fixed"},
            {5, "Reception", "Ground Floor Lobby", "Active", "2024-01-15 08:45:00", "1080p", "PTZ"},
            {6, "Hallway A", "Floor 2 - Corridor", "Active", "2024-01-15 12:30:00", "720p", "Fixed"},
            {7, "Loading Bay", "Building B - Rear", "Maintenance", "2024-01-14 14:20:00", "1080p", "Fixed"},
            {8, "Executive Floor", "Floor 5 - Restricted", "Active", "2024-01-15 07:50:00", "4K", "Dome"}
        };
        
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    private void showAddCameraDialog() {
        final JDialog addDialog = new JDialog();
        addDialog.setTitle("Add New Camera");
        addDialog.setSize(400, 500);
        addDialog.setLocationRelativeTo(this);
        addDialog.setLayout(new BorderLayout());
        addDialog.setModal(true);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Camera Name
        final JTextField nameField = new JTextField();
        formPanel.add(createLabeledField("Camera Name:", nameField));
        
        // Location
        final JTextField locationField = new JTextField();
        formPanel.add(createLabeledField("Location:", locationField));
        
        // Camera Type
        final JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Fixed", "PTZ", "Dome", "Bullet", "Thermal"});
        formPanel.add(createLabeledField("Camera Type:", typeCombo));
        
        // Resolution
        final JComboBox<String> resolutionCombo = new JComboBox<>(new String[]{"720p", "1080p", "4K", "8MP", "12MP"});
        formPanel.add(createLabeledField("Resolution:", resolutionCombo));
        
        // IP Address
        final JTextField ipField = new JTextField();
        formPanel.add(createLabeledField("IP Address:", ipField));
        
        // Port
        final JTextField portField = new JTextField("554");
        formPanel.add(createLabeledField("Port:", portField));
        
        // Status
        final JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive", "Maintenance"});
        formPanel.add(createLabeledField("Status:", statusCombo));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createStyledButton("Save Camera", new Color(40, 167, 69));
        JButton cancelBtn = createStyledButton("Cancel", new Color(108, 117, 125));
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        
        // Add action listeners
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateCameraFields(nameField, locationField, ipField, portField)) {
                    saveNewCamera(
                        nameField.getText(),
                        locationField.getText(),
                        (String) typeCombo.getSelectedItem(),
                        (String) resolutionCombo.getSelectedItem(),
                        ipField.getText(),
                        portField.getText(),
                        (String) statusCombo.getSelectedItem()
                    );
                    addDialog.dispose();
                }
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
    
    private JPanel createLabeledField(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }
    
    private boolean validateCameraFields(JTextField name, JTextField location, JTextField ip, JTextField port) {
        if (name.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter camera name", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (location.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter camera location", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ip.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter IP address", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(port.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid port number", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void saveNewCamera(String name, String location, String type, String resolution, String ip, String port, String status) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Object[] newCamera = {
            nextCameraId++,
            name,
            location,
            status,
            timestamp,
            resolution,
            type
        };
        
        tableModel.addRow(newCamera);
        JOptionPane.showMessageDialog(this, 
            "Camera added successfully!\n\n" +
            "Name: " + name + "\n" +
            "Location: " + location + "\n" +
            "IP: " + ip + ":" + port + "\n" +
            "Status: " + status,
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showEditCameraDialog() {
        int selectedRow = cameraTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a camera to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        final JDialog editDialog = new JDialog();
        editDialog.setTitle("Edit Camera - " + tableModel.getValueAt(selectedRow, 1));
        editDialog.setSize(400, 450);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new BorderLayout());
        editDialog.setModal(true);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Camera ID (read-only)
        final JTextField idField = new JTextField(tableModel.getValueAt(selectedRow, 0).toString());
        idField.setEditable(false);
        formPanel.add(createLabeledField("Camera ID:", idField));
        
        // Camera Name
        final JTextField nameField = new JTextField(tableModel.getValueAt(selectedRow, 1).toString());
        formPanel.add(createLabeledField("Camera Name:", nameField));
        
        // Location
        final JTextField locationField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString());
        formPanel.add(createLabeledField("Location:", locationField));
        
        // Camera Type
        final JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Fixed", "PTZ", "Dome", "Bullet", "Thermal"});
        typeCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 6));
        formPanel.add(createLabeledField("Camera Type:", typeCombo));
        
        // Resolution
        final JComboBox<String> resolutionCombo = new JComboBox<>(new String[]{"720p", "1080p", "4K", "8MP", "12MP"});
        resolutionCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 5));
        formPanel.add(createLabeledField("Resolution:", resolutionCombo));
        
        // Status
        final JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive", "Maintenance"});
        statusCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 3));
        formPanel.add(createLabeledField("Status:", statusCombo));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createStyledButton("Save Changes", new Color(40, 167, 69));
        JButton cancelBtn = createStyledButton("Cancel", new Color(108, 117, 125));
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        
        // Store the selected row in a final variable for use in inner class
        final int rowToEdit = selectedRow;
        
        // Add action listeners
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateEditFields(nameField, locationField)) {
                    updateCamera(rowToEdit,
                        nameField.getText(),
                        locationField.getText(),
                        (String) typeCombo.getSelectedItem(),
                        (String) resolutionCombo.getSelectedItem(),
                        (String) statusCombo.getSelectedItem()
                    );
                    editDialog.dispose();
                }
            }
        });
        
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDialog.dispose();
            }
        });
        
        editDialog.add(formPanel, BorderLayout.CENTER);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);
        editDialog.setVisible(true);
    }
    
    private boolean validateEditFields(JTextField name, JTextField location) {
        if (name.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter camera name", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (location.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter camera location", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void updateCamera(int row, String name, String location, String type, String resolution, String status) {
        tableModel.setValueAt(name, row, 1);
        tableModel.setValueAt(location, row, 2);
        tableModel.setValueAt(type, row, 6);
        tableModel.setValueAt(resolution, row, 5);
        tableModel.setValueAt(status, row, 3);
        tableModel.setValueAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), row, 4);
        
        JOptionPane.showMessageDialog(this, "Camera updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteSelectedCamera() {
        int selectedRow = cameraTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a camera to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String cameraName = tableModel.getValueAt(selectedRow, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete camera:\n" + cameraName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Camera deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showAllActiveCameras() {
        // Get all active cameras
        List<Object[]> activeCameras = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String status = tableModel.getValueAt(i, 3).toString();
            if ("Active".equals(status)) {
                Object[] cameraData = new Object[7];
                for (int j = 0; j < 7; j++) {
                    cameraData[j] = tableModel.getValueAt(i, j);
                }
                activeCameras.add(cameraData);
            }
        }
        
        if (activeCameras.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No active cameras found", "No Active Cameras", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create grid view dialog
        ActiveCamerasGridDialog gridDialog = new ActiveCamerasGridDialog(activeCameras);
        gridDialog.setVisible(true);
    }
    
    private void viewLiveFeed() {
        int selectedRow = cameraTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a camera to view", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        final String cameraName = tableModel.getValueAt(selectedRow, 1).toString();
        final String location = tableModel.getValueAt(selectedRow, 2).toString();
        final String status = tableModel.getValueAt(selectedRow, 3).toString();
        final String resolution = tableModel.getValueAt(selectedRow, 5).toString();
        
        if ("Inactive".equals(status) || "Maintenance".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "Cannot view live feed for " + status.toLowerCase() + " camera: " + cameraName,
                "Camera Not Available",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        EnhancedLiveFeedDialog liveFeedDialog = new EnhancedLiveFeedDialog(cameraName, location, resolution, status);
        liveFeedDialog.setVisible(true);
    }
    
    private void refreshCameraData() {
        // Simulate refreshing camera status and timestamps
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String status = tableModel.getValueAt(i, 3).toString();
            if ("Active".equals(status)) {
                // Update last active timestamp for active cameras
                tableModel.setValueAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), i, 4);
            }
        }
        JOptionPane.showMessageDialog(this, "Camera data refreshed!", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Custom cell renderer for status with colors
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
                        c.setBackground(new Color(212, 237, 218)); // Light green
                        c.setForeground(new Color(21, 87, 36)); // Dark green
                        break;
                    case "Inactive":
                        c.setBackground(new Color(248, 215, 218)); // Light red
                        c.setForeground(new Color(114, 28, 36)); // Dark red
                        break;
                    case "Maintenance":
                        c.setBackground(new Color(255, 243, 205)); // Light yellow
                        c.setForeground(new Color(133, 100, 4)); // Dark yellow
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                }
            }
            
            return c;
        }
    }
    
    // Enhanced Live Feed Dialog with Working Zoom and Snapshot
    class EnhancedLiveFeedDialog extends JDialog {
        private boolean isRecording = false;
        private boolean isFullScreen = false;
        private Timer recordingTimer;
        private int recordingSeconds = 0;
        private JLabel recordingTimeLabel;
        private JButton recordBtn;
        private JLabel videoFeedLabel;
        private double zoomLevel = 1.0;
        private final double ZOOM_STEP = 0.25;
        private final double MIN_ZOOM = 0.5;
        private final double MAX_ZOOM = 3.0;
        private String currentCameraName;
        private String currentLocation;
        private String currentResolution;
        private String currentStatus;
        
        public EnhancedLiveFeedDialog(String cameraName, String location, String resolution, String status) {
            currentCameraName = cameraName;
            currentLocation = location;
            currentResolution = resolution;
            currentStatus = status;
            
            setTitle("Live Feed - " + cameraName);
            setSize(900, 700);
            setLocationRelativeTo(CameraPanel.this);
            setLayout(new BorderLayout());
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            // Video panel with simulated feed
            JPanel videoPanel = createEnhancedVideoPanel();
            
            // Control panel with enhanced features
            JPanel controlPanel = createEnhancedControlPanel();
            
            add(videoPanel, BorderLayout.CENTER);
            add(controlPanel, BorderLayout.SOUTH);
            
            // Initialize recording timer
            recordingTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    recordingSeconds++;
                    updateRecordingTime();
                }
            });
        }
        
        private JPanel createEnhancedVideoPanel() {
            JPanel videoPanel = new JPanel(new BorderLayout());
            videoPanel.setBackground(Color.BLACK);
            videoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Camera info header (matching the mockup)
            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setBackground(Color.DARK_GRAY);
            infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            JLabel cameraInfoLabel = new JLabel("# " + currentCameraName, SwingConstants.LEFT);
            cameraInfoLabel.setForeground(Color.WHITE);
            cameraInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel detailsLabel = new JLabel(currentLocation + " | " + currentResolution + " | " + currentStatus, SwingConstants.LEFT);
            detailsLabel.setForeground(Color.LIGHT_GRAY);
            detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            
            // Timestamp
            JLabel timestampLabel = new JLabel(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), SwingConstants.RIGHT);
            timestampLabel.setForeground(Color.LIGHT_GRAY);
            timestampLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            
            JPanel topInfoPanel = new JPanel(new BorderLayout());
            topInfoPanel.setBackground(Color.DARK_GRAY);
            topInfoPanel.add(cameraInfoLabel, BorderLayout.WEST);
            topInfoPanel.add(timestampLabel, BorderLayout.EAST);
            
            infoPanel.add(topInfoPanel);
            infoPanel.add(detailsLabel);
            
            // Title label from mockup
            JLabel titleLabel = new JLabel("LIVE CAMERA FEED", SwingConstants.CENTER);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            
            // Simulated video content with grid overlay
            videoFeedLabel = new JLabel();
            videoFeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
            videoFeedLabel.setVerticalAlignment(SwingConstants.CENTER);
            updateVideoDisplay();
            
            // Create a panel to center the video with border
            JPanel videoContainer = new JPanel(new GridBagLayout());
            videoContainer.setBackground(Color.BLACK);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            videoContainer.add(videoFeedLabel, gbc);
            
            // Add grid overlay effect
            videoContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            
            // Recording indicator
            recordingTimeLabel = new JLabel("", SwingConstants.LEFT);
            recordingTimeLabel.setForeground(Color.RED);
            recordingTimeLabel.setFont(new Font("Arial", Font.BOLD, 12));
            recordingTimeLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            recordingTimeLabel.setVisible(false);
            
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setBackground(Color.BLACK);
            bottomPanel.add(recordingTimeLabel, BorderLayout.WEST);
            
            // Main video panel layout
            JPanel mainVideoPanel = new JPanel(new BorderLayout());
            mainVideoPanel.setBackground(Color.BLACK);
            mainVideoPanel.add(titleLabel, BorderLayout.NORTH);
            mainVideoPanel.add(videoContainer, BorderLayout.CENTER);
            mainVideoPanel.add(bottomPanel, BorderLayout.SOUTH);
            
            videoPanel.add(infoPanel, BorderLayout.NORTH);
            videoPanel.add(mainVideoPanel, BorderLayout.CENTER);
            
            return videoPanel;
        }
        
        private void updateVideoDisplay() {
            // Create a simulated video display with grid and zoom effect
            int baseWidth = 800;
            int baseHeight = 450;
            int scaledWidth = (int)(baseWidth * zoomLevel);
            int scaledHeight = (int)(baseHeight * zoomLevel);
            
            // Create a buffered image for the video display
            BufferedImage image = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            
            // Fill background
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, scaledWidth, scaledHeight);
            
            // Draw grid (like in mockup)
            g2d.setColor(new Color(50, 50, 50));
            int gridSize = 40;
            
            // Draw vertical lines
            for (int x = 0; x < scaledWidth; x += gridSize) {
                g2d.drawLine(x, 0, x, scaledHeight);
            }
            
            // Draw horizontal lines
            for (int y = 0; y < scaledHeight; y += gridSize) {
                g2d.drawLine(0, y, scaledWidth, y);
            }
            
            // Add camera information text
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, (int)(14 * zoomLevel)));
            
            String[] lines = {
                "Camera: " + currentCameraName,
                "Location: " + currentLocation,
                "Resolution: " + currentResolution,
                "Status: " + currentStatus,
                "",
                "Simulated Video Stream",
                "Grid Overlay for Reference",
                "",
                "Zoom: " + String.format("%.0f", zoomLevel * 100) + "%"
            };
            
            int lineHeight = (int)(25 * zoomLevel);
            int startY = scaledHeight / 2 - (lines.length * lineHeight) / 2;
            
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                int textWidth = g2d.getFontMetrics().stringWidth(line);
                g2d.drawString(line, (scaledWidth - textWidth) / 2, startY + (i * lineHeight));
            }
            
            g2d.dispose();
            
            // Convert to ImageIcon
            ImageIcon icon = new ImageIcon(image);
            videoFeedLabel.setIcon(icon);
        }
        
        private JPanel createEnhancedControlPanel() {
            JPanel controlPanel = new JPanel(new FlowLayout());
            controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            controlPanel.setBackground(Color.LIGHT_GRAY);
            
            // Control buttons (matching mockup layout)
            JButton fullscreenBtn = createLiveFeedButton("Fullscreen", new Color(0, 123, 255));
            JButton snapshotBtn = createLiveFeedButton("Take Snapshot", new Color(40, 167, 69));
            recordBtn = createLiveFeedButton("Start Recording", new Color(220, 53, 69));
            JButton zoomInBtn = createLiveFeedButton("Zoom In", new Color(108, 117, 125));
            JButton zoomOutBtn = createLiveFeedButton("Zoom Out", new Color(108, 117, 125));
            JButton closeBtn = createLiveFeedButton("Close", new Color(108, 117, 125));
            
            // Add action listeners
            fullscreenBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    toggleFullScreen();
                }
            });
            
            snapshotBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    takeSnapshot();
                }
            });
            
            recordBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    toggleRecording();
                }
            });
            
            zoomInBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    zoomIn();
                }
            });
            
            zoomOutBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    zoomOut();
                }
            });
            
            closeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopRecording();
                    dispose();
                }
            });
            
            controlPanel.add(fullscreenBtn);
            controlPanel.add(snapshotBtn);
            controlPanel.add(recordBtn);
            controlPanel.add(zoomInBtn);
            controlPanel.add(zoomOutBtn);
            controlPanel.add(closeBtn);
            
            return controlPanel;
        }
        
        private JButton createLiveFeedButton(String text, Color color) {
            JButton button = new JButton(text);
            button.setBackground(color);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 11));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }
        
        private void toggleFullScreen() {
            isFullScreen = !isFullScreen;
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            
            if (isFullScreen) {
                dispose();
                setUndecorated(true);
                gd.setFullScreenWindow(this);
            } else {
                setUndecorated(false);
                gd.setFullScreenWindow(null);
                setSize(900, 700);
                setLocationRelativeTo(CameraPanel.this);
            }
            setVisible(true);
        }
        
        private void takeSnapshot() {
            try {
                // Create a screenshot of the video panel
                Component videoPanel = videoFeedLabel.getParent().getParent();
                BufferedImage screenshot = new BufferedImage(
                    videoPanel.getWidth(), 
                    videoPanel.getHeight(), 
                    BufferedImage.TYPE_INT_RGB
                );
                
                Graphics2D g2d = screenshot.createGraphics();
                videoPanel.print(g2d);
                g2d.dispose();
                
                // Save to file
                String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                String safeCameraName = currentCameraName.replaceAll("[^a-zA-Z0-9]", "_");
                String filename = "snapshot_" + safeCameraName + "_" + timestamp + ".png";
                
                // Create snapshots directory if it doesn't exist
                File dir = new File("snapshots");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                
                File outputFile = new File(dir, filename);
                
                // Save the image
                javax.imageio.ImageIO.write(screenshot, "png", outputFile);
                
                // Store for possible viewing
                currentSnapshot = screenshot;
                
                // Show confirmation
                JOptionPane.showMessageDialog(this, 
                    "Snapshot saved successfully!\n\n" +
                    "File: " + filename + "\n" +
                    "Location: " + outputFile.getAbsolutePath() + "\n\n" +
                    "Would you like to view the snapshot?",
                    "Snapshot Saved", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Ask if user wants to view the snapshot
                int option = JOptionPane.showConfirmDialog(this,
                    "Open saved snapshot?",
                    "View Snapshot",
                    JOptionPane.YES_NO_OPTION);
                
                if (option == JOptionPane.YES_OPTION) {
                    openSnapshotViewer(screenshot, filename);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error saving snapshot: " + ex.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private void openSnapshotViewer(final BufferedImage image, final String filename) {
            JDialog snapshotDialog = new JDialog(this, "Snapshot: " + filename, true);
            snapshotDialog.setSize(800, 600);
            snapshotDialog.setLocationRelativeTo(this);
            
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            JScrollPane scrollPane = new JScrollPane(imageLabel);
            
            JPanel buttonPanel = new JPanel();
            JButton saveBtn = new JButton("Save As...");
            JButton closeBtn = new JButton("Close");
            
            saveBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(new File(filename));
                    int result = fileChooser.showSaveDialog(snapshotDialog);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        try {
                            File outputFile = fileChooser.getSelectedFile();
                            javax.imageio.ImageIO.write(image, "png", outputFile);
                            JOptionPane.showMessageDialog(snapshotDialog, "Image saved successfully!");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(snapshotDialog, "Error saving image: " + ex.getMessage());
                        }
                    }
                }
            });
            
            closeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    snapshotDialog.dispose();
                }
            });
            
            buttonPanel.add(saveBtn);
            buttonPanel.add(closeBtn);
            
            snapshotDialog.add(scrollPane, BorderLayout.CENTER);
            snapshotDialog.add(buttonPanel, BorderLayout.SOUTH);
            snapshotDialog.setVisible(true);
        }
        
        private void zoomIn() {
            if (zoomLevel < MAX_ZOOM) {
                zoomLevel += ZOOM_STEP;
                updateVideoDisplay();
            }
        }
        
        private void zoomOut() {
            if (zoomLevel > MIN_ZOOM) {
                zoomLevel -= ZOOM_STEP;
                updateVideoDisplay();
            }
        }
        
        private void toggleRecording() {
            if (!isRecording) {
                // Start recording
                isRecording = true;
                recordBtn.setText("Stop Recording");
                recordBtn.setBackground(new Color(220, 53, 69));
                recordingTimeLabel.setVisible(true);
                recordingSeconds = 0;
                recordingTimer.start();
                updateRecordingTime();
                JOptionPane.showMessageDialog(this, "Recording started...", "Recording", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Stop recording
                stopRecording();
            }
        }
        
        private void stopRecording() {
            if (isRecording) {
                isRecording = false;
                recordBtn.setText("Start Recording");
                recordBtn.setBackground(new Color(220, 53, 69));
                recordingTimer.stop();
                recordingTimeLabel.setVisible(false);
                
                String duration = formatTime(recordingSeconds);
                JOptionPane.showMessageDialog(this, 
                    "Recording stopped!\n\n" +
                    "Duration: " + duration + "\n" +
                    "File: recording_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".mp4\n" +
                    "Location: /recordings/",
                    "Recording Saved", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        private void updateRecordingTime() {
            String time = formatTime(recordingSeconds);
            recordingTimeLabel.setText("🔴 REC " + time);
        }
        
        private String formatTime(int seconds) {
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60;
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        }
    }
    
    // Active Cameras Grid View Dialog
    class ActiveCamerasGridDialog extends JDialog {
        private List<Object[]> activeCameras;
        
        public ActiveCamerasGridDialog(List<Object[]> activeCameras) {
            this.activeCameras = activeCameras;
            initializeUI();
        }
        
        private void initializeUI() {
            setTitle("Active Cameras Grid View");
            setSize(1200, 800);
            setLocationRelativeTo(CameraPanel.this);
            setLayout(new BorderLayout());
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            // Title
            JLabel titleLabel = new JLabel("Active Cameras Live View", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
            
            // Grid panel for camera feeds
            JPanel gridPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            gridPanel.setBackground(Color.DARK_GRAY);
            
            // Create a mini camera feed for each active camera
            for (Object[] camera : activeCameras) {
                final String cameraName = camera[1].toString();
                final String location = camera[2].toString();
                final String resolution = camera[5].toString();
                final String status = camera[3].toString();
                
                JPanel cameraFeedPanel = createMiniCameraFeed(cameraName, location, resolution, status);
                gridPanel.add(cameraFeedPanel);
            }
            
            // Wrap grid in scroll pane
            JScrollPane scrollPane = new JScrollPane(gridPanel);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Live Feeds (" + activeCameras.size() + " active cameras)"));
            
            // Control panel
            JPanel controlPanel = new JPanel(new FlowLayout());
            JButton refreshBtn = createStyledButton("Refresh All", new Color(23, 162, 184));
            JButton fullViewBtn = createStyledButton("Full View", new Color(0, 123, 255));
            JButton closeBtn = createStyledButton("Close", new Color(108, 117, 125));
            
            refreshBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    refreshGridFeeds();
                }
            });
            
            fullViewBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openSelectedInFullView();
                }
            });
            
            closeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            
            controlPanel.add(refreshBtn);
            controlPanel.add(fullViewBtn);
            controlPanel.add(closeBtn);
            
            add(titleLabel, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);
            add(controlPanel, BorderLayout.SOUTH);
        }
        
        private JPanel createMiniCameraFeed(final String cameraName, final String location, final String resolution, final String status) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.BLACK);
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            // Header with camera info
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(Color.DARK_GRAY);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            JLabel nameLabel = new JLabel("📹 " + cameraName);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
            
            JLabel statusLabel = new JLabel("● " + status);
            statusLabel.setForeground(Color.GREEN);
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            
            headerPanel.add(nameLabel, BorderLayout.WEST);
            headerPanel.add(statusLabel, BorderLayout.EAST);
            
            // Simulated video feed
            JLabel videoLabel = new JLabel();
            videoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            videoLabel.setBackground(Color.BLACK);
            videoLabel.setOpaque(true);
            
            // Create a simple simulated video
            int width = 300;
            int height = 200;
            BufferedImage simulatedVideo = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = simulatedVideo.createGraphics();
            
            // Fill with gradient
            GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 30, 60), width, height, new Color(10, 10, 30));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);
            
            // Draw grid
            g2d.setColor(new Color(80, 80, 80, 100));
            int gridSize = 20;
            for (int x = 0; x < width; x += gridSize) {
                g2d.drawLine(x, 0, x, height);
            }
            for (int y = 0; y < height; y += gridSize) {
                g2d.drawLine(0, y, width, y);
            }
            
            // Add text
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("LIVE", 10, 20);
            g2d.drawString(resolution, width - 50, 20);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 9));
            String[] locationParts = location.split(" - ");
            for (int i = 0; i < locationParts.length && i < 2; i++) {
                g2d.drawString(locationParts[i], 10, 40 + (i * 15));
            }
            
            g2d.dispose();
            
            videoLabel.setIcon(new ImageIcon(simulatedVideo));
            
            // Footer with timestamp
            final JLabel timestampLabel = new JLabel(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            timestampLabel.setForeground(Color.LIGHT_GRAY);
            timestampLabel.setFont(new Font("Arial", Font.PLAIN, 9));
            timestampLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            timestampLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            
            panel.add(headerPanel, BorderLayout.NORTH);
            panel.add(videoLabel, BorderLayout.CENTER);
            panel.add(timestampLabel, BorderLayout.SOUTH);
            
            // Add click listener to open full view
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        openFullViewForCamera(cameraName, location, resolution, status);
                    }
                }
            });
            
            return panel;
        }
        
        private void refreshGridFeeds() {
            // Update timestamps on all feeds
            Component[] components = ((JPanel)((JScrollPane)getContentPane().getComponent(1)).getViewport().getView()).getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel) {
                    Component[] subComps = ((JPanel)comp).getComponents();
                    for (Component subComp : subComps) {
                        if (subComp instanceof JLabel) {
                            JLabel label = (JLabel) subComp;
                            // Check if this is a timestamp label by its properties
                            if (label.getForeground().equals(Color.LIGHT_GRAY) && 
                                label.getFont().getName().equals("Arial") &&
                                label.getFont().getSize() == 9) {
                                label.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                            }
                        }
                    }
                }
            }
        }
        
        private void openSelectedInFullView() {
            // For simplicity, open the first camera in full view
            if (!activeCameras.isEmpty()) {
                Object[] camera = activeCameras.get(0);
                String cameraName = camera[1].toString();
                String location = camera[2].toString();
                String resolution = camera[5].toString();
                String status = camera[3].toString();
                
                openFullViewForCamera(cameraName, location, resolution, status);
            }
        }
        
        private void openFullViewForCamera(String cameraName, String location, String resolution, String status) {
            EnhancedLiveFeedDialog fullView = new EnhancedLiveFeedDialog(cameraName, location, resolution, status);
            fullView.setVisible(true);
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        JFrame frame = new JFrame("Camera Management Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        
        CameraPanel cameraPanel = new CameraPanel();
        frame.add(cameraPanel);
        
        frame.setVisible(true);
    }
}