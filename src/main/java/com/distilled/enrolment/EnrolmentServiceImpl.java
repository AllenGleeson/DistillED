package com.distilled.enrolment;

import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnrolmentServiceImpl extends EnrolmentServiceGrpc.EnrolmentServiceImplBase {
    
	private static class LocalEnrolment {
        String studentId;
        String courseId;
        boolean enrolled;

        LocalEnrolment(String studentId, String courseId, boolean enrolled) {
            this.studentId = studentId;
            this.courseId = courseId;
            this.enrolled = enrolled;
        }
    }
    // Hardcoded list of enrolments
	private final List<LocalEnrolment> enrolments = new ArrayList<>();
    // Constructor to initialize the hardcoded enrolments
	public EnrolmentServiceImpl() {
	    Random random = new Random();
	    for (int i = 1; i <= 6; i++) {
	        enrolments.add(new LocalEnrolment("user1", String.valueOf(i), random.nextBoolean()));
	    }
	}
    // Implementing the methods from the EnrolmentServiceGrpc service
    @Override
    public StreamObserver<EnrolmentRequest> manageEnrolment(StreamObserver<EnrolmentResponse> responseObserver) {
        // This method handles enrolment requests in a streaming manner
        // It allows clients to send multiple enrolment requests and receive responses for each
        return new StreamObserver<EnrolmentRequest>() {

            @Override
            public void onNext(EnrolmentRequest request) {
                String studentId = request.getStudentId();
                String courseId = request.getCourseId();
                String action = request.getAction().toLowerCase();

                String status;
                String message;
                // Handle enrolment or withdrawal based on the action
                switch (action) {
                    case "enrol":
                        if (!isEnrolled(studentId, courseId)) {
                            enrolments.add(new LocalEnrolment(studentId, courseId, true));
                            status = "success";
                            message = "Student " + studentId + " successfully enrolled in course " + courseId + ".";
                        } else {
                            status = "already_enrolled";
                            message = "Student " + studentId + " is already enrolled in course " + courseId + ".";
                        }
                        break;
                    case "withdraw":
                        if (isEnrolled(studentId, courseId)) {
                            enrolments.removeIf(e -> e.studentId.equals(studentId) && e.courseId.equals(courseId));
                            status = "success";
                            message = "Student " + studentId + " successfully withdrew from course " + courseId + ".";
                        } else {
                            status = "not_enrolled";
                            message = "Student " + studentId + " is not enrolled in course " + courseId + ".";
                        }
                        break;
                    default:
                        status = "error";
                        message = "Invalid action: " + action + ". Use 'enrol' or 'withdraw'.";
                        break;
                }

                EnrolmentResponse response = EnrolmentResponse.newBuilder()
                        .setStatus(status)
                        .setMessage(message)
                        .build();

                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Enrolment stream error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getStatus(EnrolmentRequest request, StreamObserver<EnrolmentResponse> responseObserver) {
        // This method checks if a student is enrolled in a specific course
        // It returns a response indicating the enrolment status
        String studentId = request.getStudentId();
        String courseId = request.getCourseId();
        boolean enrolled = isEnrolled(studentId, courseId);

        String status = enrolled ? "enrolled" : "not_enrolled";
        String message = enrolled
            ? "Student " + studentId + " is enrolled in course " + courseId + "."
            : "Student " + studentId + " is not enrolled in course " + courseId + ".";

        EnrolmentResponse response = EnrolmentResponse.newBuilder()
                .setStatus(status)
                .setMessage(message)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private boolean isEnrolled(String studentId, String courseId) {
        // Check if the student is enrolled in the specified course
        // This method iterates through the hardcoded enrolments to find a match
        return enrolments.stream()
                .anyMatch(e -> e.studentId.equals(studentId) && e.courseId.equals(courseId) && e.enrolled);
    }
}
