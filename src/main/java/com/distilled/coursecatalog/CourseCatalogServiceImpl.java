package com.distilled.coursecatalog;

import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;

public class CourseCatalogServiceImpl extends CourseCatalogGrpc.CourseCatalogImplBase {

    // Hardcoded list of courses
    private final List<Course> courses;

    public CourseCatalogServiceImpl() {
        courses = new ArrayList<>();
        courses.add(Course.newBuilder()
            .setId(1)
            .setTitle("Distributed Systems")
            .setDescription("Learn about distributed systems.")
            .setInstructor("Dr. Smith")
            .setDurationMinutes(120)
            .build());
        courses.add(Course.newBuilder()
            .setId(2)
            .setTitle("Algorithms")
            .setDescription("Learn about algorithms.")
            .setInstructor("Prof. Johnson")
            .setDurationMinutes(90)
            .build());
        courses.add(Course.newBuilder()
            .setId(3)
            .setTitle("Databases")
            .setDescription("Learn about databases.")
            .setInstructor("Dr. Lee")
            .setDurationMinutes(100)
            .build());
        courses.add(Course.newBuilder()
            .setId(4)
            .setTitle("Operating Systems")
            .setDescription("Learn about operating systems.")
            .setInstructor("Prof. Brown")
            .setDurationMinutes(110)
            .build());
        courses.add(Course.newBuilder()
            .setId(5)
            .setTitle("Networking")
            .setDescription("Learn about networking.")
            .setInstructor("Dr. White")
            .setDurationMinutes(95)
            .build());
        courses.add(Course.newBuilder()
            .setId(6)
            .setTitle("Machine Learning")
            .setDescription("Learn about machine learning.")
            .setInstructor("Prof. Green")
            .setDurationMinutes(130)
            .build());
    }

    @Override
    public void listCourses(Empty request, StreamObserver<CourseList> responseObserver) {
        CourseList.Builder courseListBuilder = CourseList.newBuilder();
        courseListBuilder.addAllCourses(courses); // Add all courses
        responseObserver.onNext(courseListBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCourse(CourseRequest request, StreamObserver<Course> responseObserver) {
        int courseId = request.getId();
        for (Course course : courses) {
            if (course.getId() == courseId) {
                responseObserver.onNext(course);
                responseObserver.onCompleted();
                return;
            }
        }
        // If not found, you can return an error or an empty course
        responseObserver.onError(new Exception("Course not found"));
    }
}