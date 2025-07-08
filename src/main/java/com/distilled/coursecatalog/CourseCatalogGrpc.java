package com.distilled.coursecatalog;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * The course catalog service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: course-catalog.proto")
public final class CourseCatalogGrpc {

  private CourseCatalogGrpc() {}

  public static final String SERVICE_NAME = "coursecatalog.CourseCatalog";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.distilled.coursecatalog.Empty,
      com.distilled.coursecatalog.CourseList> getListCoursesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListCourses",
      requestType = com.distilled.coursecatalog.Empty.class,
      responseType = com.distilled.coursecatalog.CourseList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.distilled.coursecatalog.Empty,
      com.distilled.coursecatalog.CourseList> getListCoursesMethod() {
    io.grpc.MethodDescriptor<com.distilled.coursecatalog.Empty, com.distilled.coursecatalog.CourseList> getListCoursesMethod;
    if ((getListCoursesMethod = CourseCatalogGrpc.getListCoursesMethod) == null) {
      synchronized (CourseCatalogGrpc.class) {
        if ((getListCoursesMethod = CourseCatalogGrpc.getListCoursesMethod) == null) {
          CourseCatalogGrpc.getListCoursesMethod = getListCoursesMethod = 
              io.grpc.MethodDescriptor.<com.distilled.coursecatalog.Empty, com.distilled.coursecatalog.CourseList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "coursecatalog.CourseCatalog", "ListCourses"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distilled.coursecatalog.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distilled.coursecatalog.CourseList.getDefaultInstance()))
                  .setSchemaDescriptor(new CourseCatalogMethodDescriptorSupplier("ListCourses"))
                  .build();
          }
        }
     }
     return getListCoursesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.distilled.coursecatalog.CourseRequest,
      com.distilled.coursecatalog.Course> getGetCourseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCourse",
      requestType = com.distilled.coursecatalog.CourseRequest.class,
      responseType = com.distilled.coursecatalog.Course.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.distilled.coursecatalog.CourseRequest,
      com.distilled.coursecatalog.Course> getGetCourseMethod() {
    io.grpc.MethodDescriptor<com.distilled.coursecatalog.CourseRequest, com.distilled.coursecatalog.Course> getGetCourseMethod;
    if ((getGetCourseMethod = CourseCatalogGrpc.getGetCourseMethod) == null) {
      synchronized (CourseCatalogGrpc.class) {
        if ((getGetCourseMethod = CourseCatalogGrpc.getGetCourseMethod) == null) {
          CourseCatalogGrpc.getGetCourseMethod = getGetCourseMethod = 
              io.grpc.MethodDescriptor.<com.distilled.coursecatalog.CourseRequest, com.distilled.coursecatalog.Course>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "coursecatalog.CourseCatalog", "GetCourse"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distilled.coursecatalog.CourseRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distilled.coursecatalog.Course.getDefaultInstance()))
                  .setSchemaDescriptor(new CourseCatalogMethodDescriptorSupplier("GetCourse"))
                  .build();
          }
        }
     }
     return getGetCourseMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CourseCatalogStub newStub(io.grpc.Channel channel) {
    return new CourseCatalogStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CourseCatalogBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CourseCatalogBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CourseCatalogFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CourseCatalogFutureStub(channel);
  }

  /**
   * <pre>
   * The course catalog service definition.
   * </pre>
   */
  public static abstract class CourseCatalogImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Returns all courses
     * </pre>
     */
    public void listCourses(com.distilled.coursecatalog.Empty request,
        io.grpc.stub.StreamObserver<com.distilled.coursecatalog.CourseList> responseObserver) {
      asyncUnimplementedUnaryCall(getListCoursesMethod(), responseObserver);
    }

    /**
     * <pre>
     * Returns a course by ID
     * </pre>
     */
    public void getCourse(com.distilled.coursecatalog.CourseRequest request,
        io.grpc.stub.StreamObserver<com.distilled.coursecatalog.Course> responseObserver) {
      asyncUnimplementedUnaryCall(getGetCourseMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getListCoursesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.distilled.coursecatalog.Empty,
                com.distilled.coursecatalog.CourseList>(
                  this, METHODID_LIST_COURSES)))
          .addMethod(
            getGetCourseMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.distilled.coursecatalog.CourseRequest,
                com.distilled.coursecatalog.Course>(
                  this, METHODID_GET_COURSE)))
          .build();
    }
  }

  /**
   * <pre>
   * The course catalog service definition.
   * </pre>
   */
  public static final class CourseCatalogStub extends io.grpc.stub.AbstractStub<CourseCatalogStub> {
    private CourseCatalogStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CourseCatalogStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CourseCatalogStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CourseCatalogStub(channel, callOptions);
    }

    /**
     * <pre>
     * Returns all courses
     * </pre>
     */
    public void listCourses(com.distilled.coursecatalog.Empty request,
        io.grpc.stub.StreamObserver<com.distilled.coursecatalog.CourseList> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListCoursesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Returns a course by ID
     * </pre>
     */
    public void getCourse(com.distilled.coursecatalog.CourseRequest request,
        io.grpc.stub.StreamObserver<com.distilled.coursecatalog.Course> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetCourseMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The course catalog service definition.
   * </pre>
   */
  public static final class CourseCatalogBlockingStub extends io.grpc.stub.AbstractStub<CourseCatalogBlockingStub> {
    private CourseCatalogBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CourseCatalogBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CourseCatalogBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CourseCatalogBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Returns all courses
     * </pre>
     */
    public com.distilled.coursecatalog.CourseList listCourses(com.distilled.coursecatalog.Empty request) {
      return blockingUnaryCall(
          getChannel(), getListCoursesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Returns a course by ID
     * </pre>
     */
    public com.distilled.coursecatalog.Course getCourse(com.distilled.coursecatalog.CourseRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetCourseMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The course catalog service definition.
   * </pre>
   */
  public static final class CourseCatalogFutureStub extends io.grpc.stub.AbstractStub<CourseCatalogFutureStub> {
    private CourseCatalogFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CourseCatalogFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CourseCatalogFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CourseCatalogFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Returns all courses
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.distilled.coursecatalog.CourseList> listCourses(
        com.distilled.coursecatalog.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getListCoursesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Returns a course by ID
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.distilled.coursecatalog.Course> getCourse(
        com.distilled.coursecatalog.CourseRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetCourseMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LIST_COURSES = 0;
  private static final int METHODID_GET_COURSE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CourseCatalogImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CourseCatalogImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_COURSES:
          serviceImpl.listCourses((com.distilled.coursecatalog.Empty) request,
              (io.grpc.stub.StreamObserver<com.distilled.coursecatalog.CourseList>) responseObserver);
          break;
        case METHODID_GET_COURSE:
          serviceImpl.getCourse((com.distilled.coursecatalog.CourseRequest) request,
              (io.grpc.stub.StreamObserver<com.distilled.coursecatalog.Course>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class CourseCatalogBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CourseCatalogBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.distilled.coursecatalog.CourseCatalogProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CourseCatalog");
    }
  }

  private static final class CourseCatalogFileDescriptorSupplier
      extends CourseCatalogBaseDescriptorSupplier {
    CourseCatalogFileDescriptorSupplier() {}
  }

  private static final class CourseCatalogMethodDescriptorSupplier
      extends CourseCatalogBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CourseCatalogMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CourseCatalogGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CourseCatalogFileDescriptorSupplier())
              .addMethod(getListCoursesMethod())
              .addMethod(getGetCourseMethod())
              .build();
        }
      }
    }
    return result;
  }
}
