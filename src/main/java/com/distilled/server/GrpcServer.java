package com.distilled.server;

import com.distilled.coursecatalog.CourseCatalogServiceImpl;
import com.distilled.enrolment.EnrolmentServiceImpl;


import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {

    public static void main(String[] args) {
        final int PORT = 50051;

        try {
            Server server = ServerBuilder.forPort(PORT)
                    .addService(new CourseCatalogServiceImpl())
                    .addService(new EnrolmentServiceImpl())
                    .build();

            server.start();
            System.out.println("✅ gRPC server started on port " + PORT);

            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("🛑 Shutting down gRPC server...");
                server.shutdown();
            }));

            // Wait for termination
            server.awaitTermination();

        } catch (Exception e) {
            System.err.println("❌ Server failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}