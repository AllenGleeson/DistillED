package com.distilled.progress;

import io.grpc.stub.StreamObserver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ProgressTrackerImpl extends ProgressTrackerGrpc.ProgressTrackerImplBase {

    private final Map<String, Float> progressStorage = new ConcurrentHashMap<>();

    @Override
    public StreamObserver<ProgressUpdateRequest> updateProgress(StreamObserver<UpdateSummaryResponse> responseObserver) {

        return new StreamObserver<ProgressUpdateRequest>() {
            int updatesProcessed = 0;
            float lastProgress = 0f;
            String latestKey = "";

            @Override
            public void onNext(ProgressUpdateRequest request) {
                float progress = request.getProgressPercent();
                if (progress < 0.0 || progress > 100.0) {
                    System.out.println("Invalid progress: " + progress);
                    return; // Ignore invalid entries
                }

                String key = request.getStudentId() + "_" + request.getCourseId() + "_" + request.getModuleName();
                progressStorage.put(key, progress);
                updatesProcessed++;
                lastProgress = progress;
                latestKey = key;
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