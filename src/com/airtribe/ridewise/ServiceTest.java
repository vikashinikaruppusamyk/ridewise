package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.*;
import com.airtribe.ridewise.service.*;
import com.airtribe.ridewise.strategy.*;

public class ServiceTest {
    public static void main(String[] args) {
        System.out.println("=== RideWise Service Layer Test ===\n");

        try {
            // Initialize services
            RiderService riderService = new RiderService();
            DriverService driverService = new DriverService();

            // Create RideService with strategies
            RideService rideService = new RideService(
                    new NearestDriverStrategy(),
                    new DefaultFareStrategy()
            );

            System.out.println("--- Step 1: Register Riders ---");
            Rider rider1 = riderService.registerRider("Vikashini", "Indiranagar");
            Rider rider2 = riderService.registerRider("Aditya", "Koramangala");
            System.out.println("✅ Registered: " + rider1.getName() + " (" + rider1.getId() + ")");
            System.out.println("✅ Registered: " + rider2.getName() + " (" + rider2.getId() + ")");
            System.out.println("Total riders: " + riderService.getTotalRiders());
            System.out.println();

            System.out.println("--- Step 2: Register Drivers ---");
            Driver driver1 = driverService.registerDriver("Ramesh", "Marathahalli");
            Driver driver2 = driverService.registerDriver("Priya", "Indiranagar");
            Driver driver3 = driverService.registerDriver("Anil", "Whitefield");
            System.out.println("✅ Registered: " + driver1.getName() + " (" + driver1.getId() + ")");
            System.out.println("✅ Registered: " + driver2.getName() + " (" + driver2.getId() + ")");
            System.out.println("✅ Registered: " + driver3.getName() + " (" + driver3.getId() + ")");
            System.out.println("Available drivers: " + driverService.getAvailableDrivers().size());
            System.out.println();

            System.out.println("--- Step 3: Request a Ride (Rider 1) ---");
            Ride ride1 = rideService.requestRide(rider1, 10.5);
            System.out.println("Ride created: " + ride1.getId());
            System.out.println("Status: " + ride1.getStatus());
            System.out.println();

            System.out.println("--- Step 4: Assign Driver (Using NearestDriverStrategy) ---");
            rideService.assignDriver(ride1, driverService);
            System.out.println("Assigned driver: " + ride1.getDriver().getName());
            System.out.println("Ride status: " + ride1.getStatus());
            System.out.println("Available drivers now: " + driverService.getAvailableDrivers().size());
            System.out.println();

            System.out.println("--- Step 5: Complete Ride (Calculate Fare) ---");
            FareReceipt receipt1 = rideService.completedRide(ride1);
            System.out.println("Fare receipt: " + receipt1);
            System.out.println("Ride status: " + ride1.getStatus());
            System.out.println("Driver available again: " + ride1.getDriver().isAvailable());
            System.out.println("Driver completed rides: " + ride1.getDriver().getCompletedRides());
            System.out.println();

            System.out.println("--- Step 6: Another Ride (Rider 2) ---");
            Ride ride2 = rideService.requestRide(rider2, 5.0);
            rideService.assignDriver(ride2, driverService);
            FareReceipt receipt2 = rideService.completedRide(ride2);
            System.out.println("Ride 2 completed. Fare: ₹" + receipt2.getAmount());
            System.out.println();

            System.out.println("--- Step 7: Switch Strategies (Peak Hour) ---");
            RideService rideServicePeakHour = new RideService(
                    new LeastActiveDriverStrategy(),
                    new PeakHourFareStrategy()
            );
            Ride ride3 = rideServicePeakHour.requestRide(rider1, 8.0);
            rideServicePeakHour.assignDriver(ride3, driverService);
            FareReceipt receipt3 = rideServicePeakHour.completedRide(ride3);
            System.out.println("Peak hour fare for 8 km: ₹" + receipt3.getAmount());
            System.out.println("✅ Same code, DIFFERENT strategy!");
            System.out.println();

            System.out.println("--- Step 8: Test NoDriverAvailableException ---");
            // Make all drivers unavailable
            driverService.updateDriverAvailability(driver1.getId(), false);
            driverService.updateDriverAvailability(driver2.getId(), false);
            driverService.updateDriverAvailability(driver3.getId(), false);

            System.out.println("Available drivers: " + driverService.getAvailableDrivers().size());
            try {
                Ride ride4 = rideService.requestRide(rider2, 5.0);
                rideService.assignDriver(ride4, driverService);
            } catch (NoDriverAvailableException e) {
                System.out.println("✅ Exception caught: " + e.getMessage());
            }
            System.out.println();

            System.out.println("--- Step 9: Statistics ---");
            System.out.println("Total rides: " + rideService.getTotalRides());
            System.out.println("Total riders: " + riderService.getTotalRiders());
            System.out.println("Total drivers: " + driverService.getTotalDrivers());
            System.out.println();

            System.out.println("✅ All service tests passed!");

        } catch (NoDriverAvailableException e) {
            System.out.println("❌ Unexpected exception: " + e.getMessage());
        }
    }
}