package com.distilled.progress;

import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;

public class ProgressTrackerImpl extends ProgressTrackerGrpc.ProgressTrackerImplBase {

    private static class LocalProgress {
        String studentId;
        String courseId;
        float percent;

        LocalProgress(String studentId, String courseId, float percent) {
            this.studentId = studentId;
            this.courseId = courseId;
            this.percent = percent;
        }
    }

    private final List<LocalProgress> progressList;

    public ProgressTrackerImpl() {
        progressList = new ArrayList<>();
        progressList.add(new LocalProgress("user1", "1", 75.0f));
        progressList.add(new LocalProgress("user1", "2", 40.0f));
        progressList.add(new LocalProgress("user1", "3", 90.0f));
        progressList.add(new LocalProgress("user1", "4", 0.0f));
        progressList.add(new LocalProgress("user1", "5", 100.0f));
        progressList.add(new LocalProgress("user1", "6", 55.0f));
    }

    @Override
    public void getProgress(ProgressQueryRequest request, StreamObserver<ProgressResponse> responseObserver) {
        float percent = 0.0f;
        for (LocalProgress progress : progressList) {
            if (progress.studentId.equals(request.getStudentId())
                    && progress.courseId.equals(request.getCourseId())) {
                percent = progress.percent;
                break;
            }
        }
        ProgressResponse response = ProgressResponse.newBuilder()
            .setPercent(percent)
            .setMessage("Progress fetched")
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ProgressUpdateRequest> updateProgress(StreamObserver<UpdateSummaryResponse> responseObserver) {
        return new StreamObserver<ProgressUpdateRequest>() {
            int updatesProcessed = 0;
            float lastProgress = 0f;
            String latestKey = "";

            @Override
            public void onNext(ProgressUpdateRequest request) {
                String studentId = request.getStudentId();
                String courseId = request.getCourseId();
                float progress = request.getProgressPercent();

                boolean found = false;
                for (int i = 0; i < progressList.size(); i++) {
                    LocalProgress p = progressList.get(i);
                    if (p.studentId.equals(studentId) && p.courseId.equals(courseId)) {
                        // Update existing
                        progressList.set(i, new LocalProgress(studentId, courseId, progress));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // Add new
                    progressList.add(new LocalProgress(studentId, courseId, progress));
                }
                updatesProcessed++;
                lastProgress = progress;
                latestKey = studentId + "_" + courseId;
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error receiving progress updates: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                String message = updatesProcessed > 0
                        ? "Progress successfully updated for " + latestKey
                        : "No valid progress updates received";

                UpdateSummaryResponse response = UpdateSummaryResponse.newBuilder()
                        .setUpdatesProcessed(updatesProcessed)
                        .setLatestProgressPercent(lastProgress)
                        .setMessage(message)
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }
}