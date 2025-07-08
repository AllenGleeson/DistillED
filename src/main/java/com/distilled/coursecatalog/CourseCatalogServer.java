package com.distilled.coursecatalog;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class CourseCatalogServer {
    public static void main(String[] args) throws Exception {
        // Create a new server to listen on port 50051
        Server server = ServerBuilder.forPort(50051)
            .addService(new CourseCatalogServiceImpl())
            .build();

        // Start the server
        server.start();
        System.out.println("CourseCatalog gRPC server started on port 50051");

        // Keep the server running
        server.awaitTermination();
    }
}