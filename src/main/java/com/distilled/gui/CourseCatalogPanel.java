package com.distilled.gui;

import javax.swing.*;
import java.awt.*;

public class CourseCatalogPanel extends JPanel {

    private final CourseListPanel courseListPanel;
    private final CourseDetailsPanel courseDetailsPanel;

    public CourseCatalogPanel() {
        setLayout(new BorderLayout());

        // Initialize components
        courseListPanel = new CourseListPanel();
        courseDetailsPanel = new CourseDetailsPanel();
        
        // Setup layout
        setupLayout();
        
        // Connect components
        courseListPanel.setSelectionListener(course -> {
            courseDetailsPanel.displayCourse(course);
        });
    }

    private void setupLayout() {
        // Add course list on the left
        add(courseListPanel, BorderLayout.WEST);
        
        // Add course details on the right
        add(courseDetailsPanel, BorderLayout.CENTER);
    }
    
    public void refresh() {
        courseListPanel.refreshCourses();
    }
}