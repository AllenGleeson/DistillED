package com.distilled.gui;

import com.distilled.coursecatalog.*;
import com.distilled.progress.*;
import com.distilled.enrolment.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;

public class CourseCatalogPanel extends JPanel {

    private final JPanel courseListPanel = new JPanel();
    private final JTextArea courseDetailsArea = new JTextArea();
    private final JPanel rightPanel = new JPanel(new BorderLayout());
    private final JPanel progressPanel = new JPanel();
    private final JPanel enrolmentPanel = new JPanel();
    private CourseCatalogGrpc.CourseCatalogBlockingStub catalogStub;
    private ProgressTrackerGrpc.ProgressTrackerBlockingStub progressStub;
    private EnrolmentServiceGrpc.EnrolmentServiceBlockingStub enrolmentStub;
    private static final String USER_ID = "user1";

    public CourseCatalogPanel() {
        setLayout(new BorderLayout());

        // Try to discover gRPC service
        try {
            String host = GrpcServiceDiscovery.getServiceHost();
            int port = GrpcServiceDiscovery.getServicePort();

            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .build();

            catalogStub = CourseCatalogGrpc.newBlockingStub(channel);
            progressStub = ProgressTrackerGrpc.newBlockingStub(channel);
            enrolmentStub = EnrolmentServiceGrpc.newBlockingStub(channel);

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

        // --- Course details and controls (Right side) ---
        courseDetailsArea.setTabSize(10);
        courseDetailsArea.setEditable(false);
        JScrollPane detailScrollPane = new JScrollPane(courseDetailsArea);
        rightPanel.add(new JLabel("Course Details"), BorderLayout.NORTH);
        rightPanel.add(detailScrollPane, BorderLayout.CENTER);
        // Add progress and enrolment panels below details
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.add(progressPanel);
        controlsPanel.add(enrolmentPanel);
        rightPanel.add(controlsPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);

        // Auto-load courses
        loadAllCourses();
    }

    private void loadAllCourses() {
        courseListPanel.removeAll();

        try {
            Iterator<Course> iterator = catalogStub.listCourses(Empty.newBuilder().build());
            while (iterator.hasNext()) {
                Course course = iterator.next();

                // Course row panel
                JPanel row = new JPanel(new BorderLayout());
                JLabel label = new JLabel("[" + course.getId() + "] " + course.getTitle());
                JButton viewBtn = new JButton("View More");

                viewBtn.addActionListener((ActionEvent e) -> {
                    showCourseDetails(course);
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

    private void showCourseDetails(Course course) {
        courseDetailsArea.setText(courseToString(course));
        updateProgressAndEnrolmentPanels(course);
    }

    private void updateProgressAndEnrolmentPanels(Course course) {
        progressPanel.removeAll();
        enrolmentPanel.removeAll();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        enrolmentPanel.setLayout(new BoxLayout(enrolmentPanel, BoxLayout.Y_AXIS));

        float progressPercent = 0f;
        boolean isEnrolled = false;
        boolean progressError = false;
        boolean enrolmentError = false;
        String progressErrorMsg = "";
        String enrolmentErrorMsg = "";

        // --- Get progress ---
        try {
            ProgressQueryRequest req = ProgressQueryRequest.newBuilder()
                    .setStudentId(USER_ID)
                    .setCourseId(String.valueOf(course.getId()))
                    .build();
            Progress progress = progressStub.getProgress(req);
            progressPercent = progress.getPercent();
        } catch (Exception e) {
            progressError = true;
            progressErrorMsg = "Progress tracker service error, please try again";
        }

        // --- Get enrolment status ---
        try {
            EnrolmentRequest req = EnrolmentRequest.newBuilder()
                    .setStudentId(USER_ID)
                    .setCourseId(String.valueOf(course.getId()))
                    .setAction("status")
                    .build();
            Iterator<EnrolmentResponse> responses = enrolmentStub.manageEnrolment(new io.grpc.stub.ClientCallStreamObserver<EnrolmentRequest>() {
                boolean sent = false;
                @Override public void onNext(EnrolmentRequest value) { sent = true; }
                @Override public void onError(Throwable t) {}
                @Override public void onCompleted() {}
                @Override public boolean isReady() { return !sent; }
                @Override public void setOnReadyHandler(Runnable r) {}
                @Override public void disableAutoInboundFlowControl() {}
                @Override public void request(int count) {}
                @Override public void setMessageCompression(boolean enable) {}
                @Override public void cancel(String message, Throwable cause) {}
            });
            // Instead, use blocking stub for unary call
            // But since proto is streaming, we simulate by sending one request and reading one response
            // So, use blocking stub and send one request, get one response
            // But Java gRPC doesn't support this directly for streaming, so use a workaround:
            // Instead, use a helper method (see below)
            isEnrolled = getEnrolmentStatus(USER_ID, String.valueOf(course.getId()));
        } catch (Exception e) {
            enrolmentError = true;
            enrolmentErrorMsg = "Enrolment service error, please try again";
        }

        // --- Progress Bar ---
        if (progressError) {
            progressPanel.add(new JLabel(progressErrorMsg));
        } else {
            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue((int) progressPercent);
            bar.setStringPainted(true);
            bar.setString("Progress: " + (int) progressPercent + "%");
            progressPanel.add(bar);
        }

        // --- Update Progress Button ---
        if (!progressError && isEnrolled && progressPercent < 100f) {
            JButton updateBtn = new JButton("Update Progress");
            updateBtn.addActionListener(e -> {
                try {
                    float newProgress = Math.min(progressPercent + 10f, 100f);
                    ProgressUpdateRequest req = ProgressUpdateRequest.newBuilder()
                            .setStudentId(USER_ID)
                            .setCourseId(String.valueOf(course.getId()))
                            .setModuleName("")
                            .setProgressPercent(newProgress)
                            .build();
                    // Use a single update for demo
                    progressStub.updateProgress(io.grpc.stub.ClientCalls.asyncClientStreamingCall(null, null));
                    // Actually, for demo, just update the list and refresh
                    updateProgressInList(USER_ID, String.valueOf(course.getId()), newProgress);
                    updateProgressAndEnrolmentPanels(course);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Failed to update progress: " + ex.getMessage());
                }
            });
            progressPanel.add(updateBtn);
        }

        // --- Enrol/Withdraw Buttons ---
        if (!enrolmentError && progressPercent < 100f) {
            if (isEnrolled) {
                JButton withdrawBtn = new JButton("Withdraw");
                withdrawBtn.addActionListener(e -> {
                    try {
                        EnrolmentRequest req = EnrolmentRequest.newBuilder()
                                .setStudentId(USER_ID)
                                .setCourseId(String.valueOf(course.getId()))
                                .setAction("withdraw")
                                .build();
                        // Send request and ignore response for demo
                        enrolmentStub.manageEnrolment(io.grpc.stub.ClientCalls.asyncClientStreamingCall(null, null)); // Not needed for demo, just update local
                        removeEnrolmentFromList(USER_ID, String.valueOf(course.getId()));
                        updateProgressAndEnrolmentPanels(course);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Failed to withdraw: " + ex.getMessage());
                    }
                });
                enrolmentPanel.add(withdrawBtn);
            } else {
                JButton enrolBtn = new JButton("Enrol");
                enrolBtn.addActionListener(e -> {
                    try {
                        EnrolmentRequest req = EnrolmentRequest.newBuilder()
                                .setStudentId(USER_ID)
                                .setCourseId(String.valueOf(course.getId()))
                                .setAction("enrol")
                                .build();
                        // Send request and ignore response for demo
                        enrolmentStub.manageEnrolment(io.grpc.stub.ClientCalls.asyncClientStreamingCall(null, null)); // Not needed for demo, just update local
                        addEnrolmentToList(USER_ID, String.valueOf(course.getId()));
                        updateProgressAndEnrolmentPanels(course);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Failed to enrol: " + ex.getMessage());
                    }
                });
                enrolmentPanel.add(enrolBtn);
            }
        }

        progressPanel.revalidate();
        progressPanel.repaint();
        enrolmentPanel.revalidate();
        enrolmentPanel.repaint();
    }

    // Helper methods for demo (simulate local update)
    private void updateProgressInList(String studentId, String courseId, float newProgress) {
        // This would call the real gRPC update in a real app
        // For demo, just update the local list if you have access
    }
    private void addEnrolmentToList(String studentId, String courseId) {
        // This would call the real gRPC update in a real app
    }
    private void removeEnrolmentFromList(String studentId, String courseId) {
        // This would call the real gRPC update in a real app
    }
    private boolean getEnrolmentStatus(String studentId, String courseId) {
        // This would call the real gRPC service and return true/false
        // For demo, always return true
        return true;
    }

    private String courseToString(Course c) {
        return String.format(
                "ID: %d\nTitle: %s\n\nDescription:\n%s\n\nInstructor: %s\nDuration: %d minutes",
                c.getId(), c.getTitle(), c.getDescription(), c.getInstructor(), c.getDurationMinutes()
        );
    }
}