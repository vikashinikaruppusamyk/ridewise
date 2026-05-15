package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.util.IdGenerator;
import java.util.ArrayList;
import java.util.List;

public class DriverService {

    // In-memory storage
    private List<Driver> drivers = new ArrayList<>();

    /**
     * Register a new driver
     */
    public Driver registerDriver(String name, String currentLocation) {
        String driverId = IdGenerator.generateDriverId();
        Driver driver = new Driver(driverId, name, currentLocation, true);  // Available by default
        drivers.add(driver);
        return driver;
    }

    /**
     * Get a driver by ID
     */
    public Driver getDriver(String driverId) {
        for (Driver driver : drivers) {
            if (driver.getId().equals(driverId)) {
                return driver;
            }
        }
        return null;  // Driver not found
    }

    /**
     * Get all available drivers
     */
    public List<Driver> getAvailableDrivers() {
        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                availableDrivers.add(driver);
            }
        }
        return availableDrivers;
    }

    /**
     * Update driver availability
     */
    public void updateDriverAvailability(String driverId, boolean available) {
        Driver driver = getDriver(driverId);
        if (driver != null) {
            driver.setAvailable(available);
        }
    }

    /**
     * Get all drivers
     */
    public List<Driver> getAllDrivers() {
        return new ArrayList<>(drivers);
    }

    /**
     * Get total drivers registered
     */
    public int getTotalDrivers() {
        return drivers.size();
    }
}