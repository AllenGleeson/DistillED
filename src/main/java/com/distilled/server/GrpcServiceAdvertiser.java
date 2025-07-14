package com.distilled.server;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;

public class GrpcServiceAdvertiser {

    private JmDNS jmdns;

    public void registerService(String serviceName, int port) {
        try {
            jmdns = JmDNS.create(InetAddress.getLocalHost());

            ServiceInfo serviceInfo = ServiceInfo.create(
                "_grpc._tcp.local.",
                serviceName,
                port,
                "path=/grpc"
            );

            jmdns.registerService(serviceInfo);

            System.out.println("gRPC service '" + serviceName + "' registered via JmDNS on port " + port);

        } catch (IOException e) {
            System.err.println("Failed to register gRPC service with JmDNS: " + e.getMessage());
        }
    }

    public void unregisterService() {
        if (jmdns != null) {
            jmdns.unregisterAllServices();
            try {
                jmdns.close();
                System.out.println("JmDNS service unregistered and closed.");
            } catch (IOException e) {
                System.err.println("Error closing JmDNS: " + e.getMessage());
            }
        }
    }
}