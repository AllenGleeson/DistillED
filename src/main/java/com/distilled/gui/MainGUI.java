package com.distilled.gui;

import javax.swing.*;
import java.awt.*;

// Main GUI class that initializes the application window
public class MainGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    public MainGUI() {
        // Set up the main frame
        setTitle("DistillED Services");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        // Add the CourseCatalogPanel to the tabs
        CourseCatalogPanel courseCatalogPanel = new CourseCatalogPanel();
        courseCatalogPanel.setBackground(new Color(49, 40, 155));
        tabs.addTab("Course Catalog", courseCatalogPanel);

        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}