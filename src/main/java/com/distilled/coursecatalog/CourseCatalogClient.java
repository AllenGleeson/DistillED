package com.distilled.coursecatalog;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CourseCatalogClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext()
            .build();

        CourseCatalogGrpc.CourseCatalogBlockingStub stub = CourseCatalogGrpc.newBlockingStub(channel);

        // Call listCourses
        CourseList courseList = stub.listCourses(Empty.newBuilder().build());
        System.out.println("All courses:");
        for (Course course : courseList.getCoursesList()) {
            System.out.println(course);
        }

        // Call getCourse
        Course course = stub.getCourse(CourseRequest.newBuilder().setId(1).build());
        System.out.println("Course with ID 1:" + course);
        channel.shutdown();
    }
}