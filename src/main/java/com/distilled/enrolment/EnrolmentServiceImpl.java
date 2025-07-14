package com.distilled.enrolment;

import io.grpc.stub.StreamObserver;

public class EnrolmentServiceImpl extends EnrolmentServiceGrpc.EnrolmentServiceImplBase {

    @Override
    public StreamObserver<EnrolmentRequest> manageEnrolment(StreamObserver<EnrolmentResponse> responseObserver) {
        return new StreamObserver<EnrolmentRequest>() {

            @Override
            public void onNext(EnrolmentRequest request) {
                String studentId = request.getStudentId();
                String courseId = request.getCourseId();
                String action = request.getAction().toLowerCase();

                String status;
                String message;

                switch (action) {
                    case "enrol":
                        status = "success";
                        message = "Student " + studentId + " successfully enrolled in course " + courseId + ".";
                        break;
                    case "withdraw":
                        status = "success";
                        message = "Student " + studentId + " successfully withdrew from course " + courseId + ".";
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
}
