package com.distilled.gui;

import com.distilled.coursecatalog.Course;

import javax.swing.*;
import java.awt.*;

// This panel displays course details and connects to progress and enrolment panels
public class CourseDetailsPanel extends JPanel {
    
    private final JTextArea courseDetailsArea = new JTextArea();
    private final JPanel rightPanel = new JPanel(new BorderLayout());
    private final ProgressPanel progressPanel;
    private final EnrolmentPanel enrolmentPanel;
    
    public CourseDetailsPanel() {
        setLayout(new BorderLayout());
        
        // Initialize sub-panels
        progressPanel = new ProgressPanel();
        enrolmentPanel = new EnrolmentPanel();
        
        // Connect panels for cross-communication
        enrolmentPanel.setProgressPanel(progressPanel);
        
        setupRightPanel();
    }
    
    private void setupRightPanel() {
        // Course details area
        courseDetailsArea.setEditable(false);
        courseDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        courseDetailsArea.setTabSize(4);
        JScrollPane detailScrollPane = new JScrollPane(courseDetailsArea);
        detailScrollPane.setPreferredSize(new Dimension(400, 200));
        
        // Combine panels
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(progressPanel, BorderLayout.NORTH);
        bottomPanel.add(enrolmentPanel, BorderLayout.SOUTH);
        
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(new JLabel("Course Details", SwingConstants.CENTER), BorderLayout.NORTH);
        rightPanel.add(detailScrollPane, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(rightPanel, BorderLayout.CENTER);
    }
    
    public void displayCourse(Course course) {
        courseDetailsArea.setText(formatCourseDetails(course));
        progressPanel.updateForCourse(course);
        enrolmentPanel.updateForCourse(course);
    }
    
    private String formatCourseDetails(Course course) {
        return String.format(
            "Course ID: %d\n" +
            "Title: %s\n" +
            "Instructor: %s\n" +
            "Duration: %d minutes\n\n" +
            "Description:\n%s",
            course.getId(),
            course.getTitle(),
            course.getInstructor(),
            course.getDurationMinutes(),
            course.getDescription()
        );
    }
    
    public ProgressPanel getProgressPanel() {
        return progressPanel;
    }
    
    public EnrolmentPanel getEnrolmentPanel() {
        return enrolmentPanel;
    }
} 