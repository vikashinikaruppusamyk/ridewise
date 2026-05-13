package com.airtribe.ridewise.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static AtomicInteger riderCounter = new AtomicInteger(1000);
    private static AtomicInteger driverCounter = new AtomicInteger(2000);
    private static AtomicInteger rideCounter = new AtomicInteger(5000);

    public static String generateRiderId() {
        return "RIDER_" + riderCounter.incrementAndGet();
    }

    public static String generateDriverId() {
        return "DRIVER_" + driverCounter.incrementAndGet();
    }

    public static String generateRideId() {
        return "RIDE_" + rideCounter.incrementAndGet();
    }
}