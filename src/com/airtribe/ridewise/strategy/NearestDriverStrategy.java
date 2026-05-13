package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import java.util.List;

public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> availableDrivers) {
        if (availableDrivers == null || availableDrivers.isEmpty()) {
            return null;
        }

        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;

        for (Driver driver : availableDrivers) {
            double distance = calculateDistance(
                    driver.getCurrentLocation(),
                    rider.getLocation()
            );

            if (distance < minDistance) {
                minDistance = distance;
                nearestDriver = driver;
            }
        }

        return nearestDriver;
    }

    private double calculateDistance(String location1, String location2) {
        // Simple distance calculation (in real world, use GPS/maps API)
        // For now, use hash-based pseudo distance
        return Math.abs(location1.hashCode() - location2.hashCode()) % 100.0;
    }
}