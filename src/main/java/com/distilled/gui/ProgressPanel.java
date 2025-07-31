package com.distilled.gui;

import com.distilled.coursecatalog.Course;
import com.distilled.progress.*;
import com.distilled.enrolment.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ProgressPanel extends JPanel {
    
    private ProgressTrackerGrpc.ProgressTrackerBlockingStub progressBlockingStub;
    private ProgressTrackerGrpc.ProgressTrackerStub progressStub;
    private EnrolmentServiceGrpc.EnrolmentServiceBlockingStub enrolmentStub;
    private static final String USER_ID = "user1";
    private Course currentCourse = null;
    
    public ProgressPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Progress"));
        initializeGrpcConnections();
    }
    
    private void initializeGrpcConnections() {
        try {
            String host = GrpcServiceDiscovery.getServiceHost();
            int port = GrpcServiceDiscovery.getServicePort();

            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .build();

            progressBlockingStub = ProgressTrackerGrpc.newBlockingStub(channel);
            progressStub = ProgressTrackerGrpc.newStub(channel);
            enrolmentStub = EnrolmentServiceGrpc.newBlockingStub(channel);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to discover gRPC service: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateForCourse(Course course) {
        currentCourse = course;
        removeAll();
        
        // Check enrolment status first
        boolean isEnrolled = checkEnrolmentStatus(course.getId());
        
        // Show progress only if enrolled
        if (isEnrolled) {
            showProgressBar(course.getId());
        } else {
            add(new JLabel("Not enrolled - no progress to show"));
        }
        
        revalidate();
        repaint();
    }
    
    private boolean checkEnrolmentStatus(int courseId) {
        try {
            EnrolmentRequest request = EnrolmentRequest.newBuilder()
                    .setStudentId(USER_ID)
                    .setCourseId(String.valueOf(courseId))
                    .setAction("status")
                    .build();
            
            EnrolmentResponse response = enrolmentStub.getStatus(request);
            return "enrolled".equals(response.getStatus());
        } catch (Exception e) {
            System.err.println("Error checking enrolment status: " + e.getMessage());
            return false;
        }
    }
    
    private void showProgressBar(int courseId) {
        try {
            ProgressQueryRequest request = ProgressQueryRequest.newBuilder()
                    .setStudentId(USER_ID)
                    .setCourseId(String.valueOf(courseId))
                    .build();
            
            ProgressResponse response = progressBlockingStub.getProgress(request);
            float progressPercent = response.getPercent();

            // Progress bar
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue((int) progressPercent);
            progressBar.setStringPainted(true);
            progressBar.setString(String.format("Progress: %.1f%%", progressPercent));
            add(progressBar);
            
            // Update progress button (only if not 100%)
            if (progressPercent < 100.0f) {
                JButton updateBtn = new JButton("Update Progress (+10%)");
                updateBtn.addActionListener(e -> updateProgress(courseId, progressPercent));
                add(updateBtn);
            }

        } catch (Exception e) {
            add(new JLabel("Error loading progress: " + e.getMessage()));
        }
    }
    
    private void updateProgress(int courseId, float currentProgress) {
        try {
            float newProgress = Math.min(currentProgress + 10.0f, 100.0f);
            
            // Create a CountDownLatch to wait for the streaming response
            CountDownLatch latch = new CountDownLatch(1);
            final boolean[] success = {false};
            
            StreamObserver<ProgressUpdateRequest> requestObserver = 
                progressStub.updateProgress(new StreamObserver<UpdateSummaryResponse>() {
                    @Override
                    public void onNext(UpdateSummaryResponse response) {
                        success[0] = true;
                        latch.countDown();
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println("Progress update error: " + t.getMessage());
                        latch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        latch.countDown();
                    }
                });

            // Send the update request
            ProgressUpdateRequest request = ProgressUpdateRequest.newBuilder()
                    .setStudentId(USER_ID)
                    .setCourseId(String.valueOf(courseId))
                    .setModuleName("Module")
                    .setProgressPercent(newProgress)
                    .build();
            
            requestObserver.onNext(request);
            requestObserver.onCompleted();

            // Wait for response
            if (latch.await(5, TimeUnit.SECONDS) && success[0]) {
                // Refresh the display silently
                if (currentCourse != null) {
                    updateForCourse(currentCourse);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to update progress", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error updating progress: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} 