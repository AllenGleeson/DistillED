package com.distilled.gui;

import com.distilled.coursecatalog.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;

// This panel displays a list of courses and allows selection to view details
public class CourseListPanel extends JPanel {

    private final JPanel courseListPanel = new JPanel();
    private CourseCatalogGrpc.CourseCatalogBlockingStub catalogStub;
    private CourseSelectionListener selectionListener;

    public interface CourseSelectionListener {
        void onCourseSelected(Course course);
    }

    public CourseListPanel() {
        setLayout(new BorderLayout());
        initializeGrpcConnection();
        setupCourseListPanel();
        loadAllCourses();
    }

    public void setSelectionListener(CourseSelectionListener listener) {
        this.selectionListener = listener;
    }

    private void initializeGrpcConnection() {
        // Discover gRPC service using service discovery
        try {
            String host = GrpcServiceDiscovery.getServiceHost();
            int port = GrpcServiceDiscovery.getServicePort();

            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .build();

            catalogStub = CourseCatalogGrpc.newBlockingStub(channel);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to discover gRPC service: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Set up the course list panel layout
    private void setupCourseListPanel() {
        courseListPanel.setLayout(new BoxLayout(courseListPanel, BoxLayout.Y_AXIS));
        courseListPanel.setBorder(BorderFactory.createTitledBorder("Available Courses"));

        JScrollPane listScrollPane = new JScrollPane(courseListPanel);
        listScrollPane.setPreferredSize(new Dimension(400, 0));
        add(listScrollPane, BorderLayout.CENTER);
    }

    // Load all courses from the catalog service
    private void loadAllCourses() {
        courseListPanel.removeAll();

        try {
            Iterator<Course> iterator = catalogStub.listCourses(Empty.newBuilder().build());
            while (iterator.hasNext()) {
                Course course = iterator.next();
                addCourseToList(course);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load courses: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        courseListPanel.revalidate();
        courseListPanel.repaint();
    }

    // Add a course to the list panel with a button to view details
    private void addCourseToList(Course course) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel label = new JLabel(String.format("[%d] %s", course.getId(), course.getTitle()));
        JButton viewBtn = new JButton("View");

        viewBtn.addActionListener((ActionEvent e) -> {
            if (selectionListener != null) {
                selectionListener.onCourseSelected(course);
            }
        });

        row.add(label, BorderLayout.CENTER);
        row.add(viewBtn, BorderLayout.EAST);
        courseListPanel.add(row);
    }

    // Refresh the course list by reloading from the service
    public void refreshCourses() {
        loadAllCourses();
    }
}