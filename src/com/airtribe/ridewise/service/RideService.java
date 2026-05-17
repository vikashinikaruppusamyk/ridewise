package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;
import java.util.ArrayList;
import java.util.List;

public class RideService {

    // In-memory storage
    private List<Ride> rides = new ArrayList<>();
    private List<FareReceipt> receipts = new ArrayList<>();

    // Injected strategies
    private RideMatchingStrategy rideMatchingStrategy;
    private FareStrategy fareStrategy;

    /**
     * Constructor with dependency injection
     * This is the key to flexibility!
     */
    public RideService(RideMatchingStrategy rideMatchingStrategy, FareStrategy fareStrategy) {
        this.rideMatchingStrategy = rideMatchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    /**
     * Step 1: Create a new ride request
     */
    public Ride requestRide(Rider rider, double distance) {
        String rideId = IdGenerator.generateRideId();
        Ride ride = new Ride(rideId, rider, distance);
        rides.add(ride);
        System.out.println("📍 Ride requested: " + rideId);
        return ride;
    }

    /**
     * Step 2: Find and assign a driver using the strategy
     */
    public void assignDriver(Ride ride, DriverService driverService)
            throws NoDriverAvailableException {

        // Get all available drivers
        List<Driver> availableDrivers = driverService.getAvailableDrivers();

        if (availableDrivers.isEmpty()) {
            throw new NoDriverAvailableException(
                    "Sorry! No drivers available in " + ride.getRider().getLocation() + " right now."
            );
        }

        // Use the injected strategy to find a driver
        Driver assignedDriver = rideMatchingStrategy.findDriver(
                ride.getRider(),
                availableDrivers
        );

        if (assignedDriver == null) {
            throw new NoDriverAvailableException(
                    "Unable to find a suitable driver."
            );
        }

        // Assign driver to ride
        ride.setDriver(assignedDriver);
        ride.setStatus(RideStatus.ASSIGNED);

        // Mark driver as unavailable
        assignedDriver.setAvailable(false);

        System.out.println("🚗 Driver assigned: " + assignedDriver.getName());
    }

    /**
     * Step 3: Complete the ride and calculate fare
     */
    public FareReceipt completedRide(Ride ride) {
        if (ride.getDriver() == null) {
            System.out.println("❌ Error: No driver assigned to this ride!");
            return null;
        }

        // Use the injected strategy to calculate fare
        double fare = fareStrategy.calculateFare(ride);

        // Create receipt
        FareReceipt receipt = new FareReceipt(ride.getId(), fare);
        receipts.add(receipt);

        // Update ride status
        ride.setStatus(RideStatus.COMPLETED);

        // Update driver
        Driver driver = ride.getDriver();
        driver.incrementCompletedRides();
        driver.setAvailable(true);  // Driver is available again

        System.out.println("✅ Ride completed. Fare: ₹" + fare);

        return receipt;
    }

    /**
     * Cancel a ride
     */
    public void cancelRide(Ride ride) {
        ride.setStatus(RideStatus.CANCELLED);

        // Make driver available again if they were assigned
        if (ride.getDriver() != null) {
            ride.getDriver().setAvailable(true);
        }

        System.out.println("❌ Ride cancelled: " + ride.getId());
    }

    /**
     * Get a ride by ID
     */
    public Ride getRide(String rideId) {
        for (Ride ride : rides) {
            if (ride.getId().equals(rideId)) {
                return ride;
            }
        }
        return null;
    }

    /**
     * Get all rides
     */
    public List<Ride> getAllRides() {
        return new ArrayList<>(rides);
    }

    /**
     * Get a receipt by ride ID
     */
    public FareReceipt getReceipt(String rideId) {
        for (FareReceipt receipt : receipts) {
            if (receipt.getRideId().equals(rideId)) {
                return receipt;
            }
        }
        return null;
    }

    /**
     * Get total rides completed
     */
    public int getTotalRides() {
        return rides.size();
    }
}