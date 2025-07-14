package com.distilled.gui;

import com.distilled.coursecatalog.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;

public class CourseCatalogPanel extends JPanel {

    private final JPanel courseListPanel = new JPanel();
    private final JTextArea courseDetailsArea = new JTextArea();
    private CourseCatalogGrpc.CourseCatalogBlockingStub stub;

    public CourseCatalogPanel() {
        setLayout(new BorderLayout());

        // Try to discover gRPC service
        try {
            String host = GrpcServiceDiscovery.getServiceHost();
            int port = GrpcServiceDiscovery.getServicePort();

            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .build();

            stub = CourseCatalogGrpc.newBlockingStub(channel);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to discover gRPC service: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Course List Panel (Left side) ---
        courseListPanel.setLayout(new BoxLayout(courseListPanel, BoxLayout.Y_AXIS));
        JScrollPane listScrollPane = new JScrollPane(courseListPanel);
        listScrollPane.setPreferredSize(new Dimension(400, 0));
        add(listScrollPane, BorderLayout.CENTER);

        // --- Course details (Right side) ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        courseDetailsArea.setTabSize(10);
        courseDetailsArea.setEditable(false);
        JScrollPane detailScrollPane = new JScrollPane(courseDetailsArea);
        rightPanel.add(new JLabel("Course Details"), BorderLayout.NORTH);
        rightPanel.add(detailScrollPane, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Auto-load courses
        loadAllCourses();
    }

    private void loadAllCourses() {
        courseListPanel.removeAll();

        try {
            Iterator<Course> iterator = stub.listCourses(Empty.newBuilder().build());
            while (iterator.hasNext()) {
                Course course = iterator.next();

                // Course row panel
                JPanel row = new JPanel(new BorderLayout());
                JLabel label = new JLabel("[" + course.getId() + "] " + course.getTitle());
                JButton viewBtn = new JButton("View More");

                viewBtn.addActionListener((ActionEvent e) -> {
                    getCourseById(course.getId());
                });

                row.add(label, BorderLayout.CENTER);
                row.add(viewBtn, BorderLayout.EAST);
                row.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                courseListPanel.add(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load courses: " + e.getMessage());
        }

        revalidate();
        repaint();
    }

    private void getCourseById(int id) {
        try {
            Course course = stub.getCourse(CourseRequest.newBuilder().setId(id).build());
            courseDetailsArea.setText(courseToString(course));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Course not found: " + e.getMessage());
        }
    }

    private String courseToString(Course c) {
        return String.format(
                "ID: %d\nTitle: %s\n\nDescription:\n%s\n\nInstructor: %s\nDuration: %d minutes",
                c.getId(), c.getTitle(), c.getDescription(), c.getInstructor(), c.getDurationMinutes()
        );
    }
}