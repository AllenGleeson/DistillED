package com.distilled.gui;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;

public class GrpcServiceDiscovery {

    private static final String SERVICE_TYPE = "_grpc._tcp.local.";
    private static final String SERVICE_NAME = "DistillED-gRPC";

    private static ServiceInfo cachedServiceInfo;

    // Public method used by GUI to get host
    public static String getServiceHost() {
        ServiceInfo info = discoverService();
        if (info != null && info.getHostAddresses().length > 0) {
            return info.getHostAddresses()[0];
        }
        return "localhost";
    }

    // Public method used by GUI to get port
    public static int getServicePort() {
        ServiceInfo info = discoverService();
        if (info != null) {
            return info.getPort();
        }
        return 50051; // fallback
    }

    // Internal method to perform actual discovery
    public static ServiceInfo discoverService() {
        if (cachedServiceInfo != null) {
            return cachedServiceInfo;
        }

        try {
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

            final CountDownLatch latch = new CountDownLatch(1);
            final ServiceInfo[] discovered = new ServiceInfo[1];

            jmdns.addServiceListener(SERVICE_TYPE, new ServiceListener() {
                public void serviceAdded(ServiceEvent event) {
                    if (event.getName().equals(SERVICE_NAME)) {
                        jmdns.requestServiceInfo(SERVICE_TYPE, SERVICE_NAME);
                    }
                }

                public void serviceResolved(ServiceEvent event) {
                    if (event.getName().equals(SERVICE_NAME)) {
                        discovered[0] = event.getInfo();
                        latch.countDown();
                    }
                }

                public void serviceRemoved(ServiceEvent event) {
                    // Not needed for discovery
                }
            });

            latch.await();
            cachedServiceInfo = discovered[0];
            return cachedServiceInfo;

        } catch (IOException | InterruptedException e) {
            System.err.println("Service discovery failed: " + e.getMessage());
            return null;
        }
    }
}