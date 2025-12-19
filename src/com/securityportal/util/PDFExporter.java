package com.securityportal.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.List;

public class PDFExporter {
    
    public static boolean exportSecurityIncidentReport(String filePath, String title, 
                                                     List<String> summary, String[] columns, 
                                                     List<String[]> data) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(20);
            document.add(titlePara);
            
            // Add summary section
            Font summaryFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            for (String summaryLine : summary) {
                Paragraph summaryPara = new Paragraph(summaryLine, summaryFont);
                summaryPara.setSpacingAfter(5);
                document.add(summaryPara);
            }
            
            // Add space between summary and table
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            
            // Create table
            PdfPTable table = new PdfPTable(columns.length);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            
            // Set column widths if needed (optional)
            float[] columnWidths = new float[columns.length];
            for (int i = 0; i < columns.length; i++) {
                columnWidths[i] = 1f;
            }
            table.setWidths(columnWidths);
            
            // Add table headers
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            for (String column : columns) {
                PdfPCell header = new PdfPCell(new Phrase(column, headerFont));
                header.setBackgroundColor(BaseColor.DARK_GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setPadding(8);
                header.setBorderWidth(1f);
                table.addCell(header);
            }
            
            // Add table data
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 10);
            for (String[] row : data) {
                for (String cell : row) {
                    PdfPCell dataCell = new PdfPCell(new Phrase(cell, dataFont));
                    dataCell.setPadding(6);
                    dataCell.setBorderWidth(0.5f);
                    dataCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    table.addCell(dataCell);
                }
            }
            
            document.add(table);
            
            // Add footer with timestamp
            document.add(Chunk.NEWLINE);
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC);
            Paragraph footer = new Paragraph("Generated on: " + new java.util.Date(), footerFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);
            
            document.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error exporting PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Additional method for different report types
    public static boolean exportCustomReport(String filePath, String title, 
                                           List<String> content, String[] columns, 
                                           List<String[]> data) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(15);
            document.add(titlePara);
            
            // Add content lines
            Font contentFont = new Font(Font.FontFamily.HELVETICA, 10);
            for (String line : content) {
                Paragraph contentPara = new Paragraph(line, contentFont);
                contentPara.setSpacingAfter(3);
                document.add(contentPara);
            }
            
            // Add table if columns and data are provided
            if (columns != null && data != null && columns.length > 0) {
                document.add(Chunk.NEWLINE);
                
                PdfPTable table = new PdfPTable(columns.length);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                
                // Table headers
                Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
                for (String column : columns) {
                    PdfPCell header = new PdfPCell(new Phrase(column, headerFont));
                    header.setBackgroundColor(BaseColor.GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setPadding(6);
                    table.addCell(header);
                }
                
                // Table data
                Font dataFont = new Font(Font.FontFamily.HELVETICA, 9);
                for (String[] row : data) {
                    for (String cell : row) {
                        PdfPCell dataCell = new PdfPCell(new Phrase(cell, dataFont));
                        dataCell.setPadding(4);
                        table.addCell(dataCell);
                    }
                }
                
                document.add(table);
            }
            
            document.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error exporting custom PDF report: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to create a simple report with just text
    public static boolean exportTextReport(String filePath, String title, List<String> lines) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            document.add(titlePara);
            
            document.add(Chunk.NEWLINE);
            
            // Content lines
            Font contentFont = new Font(Font.FontFamily.HELVETICA, 11);
            for (String line : lines) {
                Paragraph para = new Paragraph(line, contentFont);
                para.setSpacingAfter(5);
                document.add(para);
            }
            
            document.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error exporting text PDF report: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}