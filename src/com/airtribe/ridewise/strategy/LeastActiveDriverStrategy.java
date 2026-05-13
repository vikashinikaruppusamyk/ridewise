package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import java.util.List;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> availableDrivers) {
        if (availableDrivers == null || availableDrivers.isEmpty()) {
            return null;
        }

        Driver leastActiveDriver = null;
        int minRides = Integer.MAX_VALUE;

        for (Driver driver : availableDrivers) {
            if (driver.getCompletedRides() < minRides) {
                minRides = driver.getCompletedRides();
                leastActiveDriver = driver;
            }
        }

        return leastActiveDriver;
    }
}