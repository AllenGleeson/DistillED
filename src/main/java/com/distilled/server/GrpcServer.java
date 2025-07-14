package com.distilled.server;

import java.io.IOException;

import com.distilled.coursecatalog.CourseCatalogServiceImpl;
import com.distilled.enrolment.EnrolmentServiceImpl;


import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051;

        Server server = ServerBuilder.forPort(port)
            .addService(new CourseCatalogServiceImpl())
            .addService(new EnrolmentServiceImpl())
            .build()
            .start();

        // Register service using JmDNS
        GrpcServiceAdvertiser advertiser = new GrpcServiceAdvertiser();
        advertiser.registerService("DistillED-gRPC", port);

        System.out.println("Server started on port " + port);
        server.awaitTermination();

        // Unregister when server stops
        Runtime.getRuntime().addShutdownHook(new Thread(advertiser::unregisterService));
    }
}