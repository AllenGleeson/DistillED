package com.distilled.gui;

import com.distilled.coursecatalog.Course;
import com.distilled.enrolment.*;
import com.distilled.progress.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class EnrolmentPanel extends JPanel {
    
    private EnrolmentServiceGrpc.EnrolmentServiceStub enrolmentStub;
    private EnrolmentServiceGrpc.EnrolmentServiceBlockingStub enrolmentBlockingStub;
    private ProgressTrackerGrpc.ProgressTrackerBlockingStub progressStub;
    private static final String USER_ID = "user1";
    private Course currentCourse = null;
    private ProgressPanel progressPanel;
    
    public EnrolmentPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Enrolment"));
        initializeGrpcConnections();
    }
    
    public void setProgressPanel(ProgressPanel progressPanel) {
        this.progressPanel = progressPanel;
    }
    
    private void initializeGrpcConnections() {
        try {
            String host = GrpcServiceDiscovery.getServiceHost();
            int port = GrpcServiceDiscovery.getServicePort();

            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .build();

            enrolmentStub = EnrolmentServiceGrpc.newStub(channel);
            enrolmentBlockingStub = EnrolmentServiceGrpc.newBlockingStub(channel);
            progressStub = ProgressTrackerGrpc.newBlockingStub(channel);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to discover gRPC service: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateForCourse(Course course) {
        currentCourse = course;
        removeAll();
        
        // Check enrolment status
        boolean isEnrolled = checkEnrolmentStatus(course.getId());
        showEnrolmentControls(course.getId(), isEnrolled);
        
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
            
            EnrolmentResponse response = enrolmentBlockingStub.getStatus(request);
            return "enrolled".equals(response.getStatus());
        } catch (Exception e) {
            System.err.println("Error checking enrolment status: " + e.getMessage());
            return false;
        }
    }
    
    private void showEnrolmentControls(int courseId, boolean isEnrolled) {
        if (isEnrolled) {
            // Check if progress is 100% - if so, don't show withdraw button
            float progress = getProgressPercent(courseId);
            if (progress < 100.0f) {
                JButton withdrawBtn = new JButton("Withdraw from Course");
                withdrawBtn.addActionListener(e -> manageEnrolment(courseId, "withdraw"));
                add(withdrawBtn);
            } else {
                add(new JLabel("Course completed - cannot withdraw"));
            }
        } else {
            JButton enrolBtn = new JButton("Enrol in Course");
            enrolBtn.addActionListener(e -> manageEnrolment(courseId, "enrol"));
            add(enrolBtn);
        }
    }
    
    private float getProgressPercent(int courseId) {
        try {
            ProgressQueryRequest request = ProgressQueryRequest.newBuilder()
                    .setStudentId(USER_ID)
                    .setCourseId(String.valueOf(courseId))
                    .build();
            
            ProgressResponse response = progressStub.getProgress(request);
            return response.getPercent();
        } catch (Exception e) {
            System.err.println("Error getting progress: " + e.getMessage());
            return 0.0f;
        }
    }
    
    private void manageEnrolment(int courseId, String action) {
        try {
            // Create a CountDownLatch to wait for the streaming response
            CountDownLatch latch = new CountDownLatch(1);
            final String[] resultMessage = {""};
            
            StreamObserver<EnrolmentRequest> requestObserver = 
                enrolmentStub.manageEnrolment(new StreamObserver<EnrolmentResponse>() {
                    @Override
                    public void onNext(EnrolmentResponse response) {
                        resultMessage[0] = response.getMessage();
                        latch.countDown();
                    }

                    @Override
                    public void onError(Throwable t) {
                        resultMessage[0] = "Error: " + t.getMessage();
                        latch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        latch.countDown();
                    }
                });

            // Send the enrolment request
            EnrolmentRequest request = EnrolmentRequest.newBuilder()
                    .setStudentId(USER_ID)
                    .setCourseId(String.valueOf(courseId))
                    .setAction(action)
                    .build();
            
            requestObserver.onNext(request);
            requestObserver.onCompleted();

            // Wait for response
            if (latch.await(5, TimeUnit.SECONDS)) {
                // Refresh the display silently
                if (currentCourse != null) {
                    updateForCourse(currentCourse);
                    if (progressPanel != null) {
                        progressPanel.updateForCourse(currentCourse);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Timeout waiting for enrolment response", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error managing enrolment: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} 