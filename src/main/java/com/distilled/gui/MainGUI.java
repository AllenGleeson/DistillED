package com.distilled.gui;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    public MainGUI() {
        setTitle("DistillED Services");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        CourseCatalogPanel courseCatalogPanel = new CourseCatalogPanel();
        courseCatalogPanel.setBackground(new Color(49, 40, 155));
        tabs.addTab("Course Catalog", courseCatalogPanel);

        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}