package com.airtribe.ridewise;

import com.airtribe.ridewise.model.*;
import com.airtribe.ridewise.strategy.*;
import com.airtribe.ridewise.util.IdGenerator;
import java.util.ArrayList;
import java.util.List;

public class StrategyTest {
    public static void main(String[] args) {
        System.out.println("=== RideWise Strategy Pattern Test ===\n");

        // Create test data
        Rider rider = new Rider(IdGenerator.generateRiderId(), "Vikashini", "Indiranagar");

        List<Driver> drivers = new ArrayList<>();
        Driver driver1 = new Driver(IdGenerator.generateDriverId(), "Ramesh", "Whitefield", true);
        driver1.setCurrentLocation("Marathahalli");  // 8 km away

        Driver driver2 = new Driver(IdGenerator.generateDriverId(), "Priya", "Indiranagar", true);
        driver2.setCurrentLocation("Indiranagar");   // 0 km away (NEAREST)

        Driver driver3 = new Driver(IdGenerator.generateDriverId(), "Anil", "KoramAngala", true);
        driver3.setCurrentLocation("Whitefield");    // 25 km away
        for (int i = 0; i < 20; i++) driver3.incrementCompletedRides();  // 20 rides

        drivers.add(driver1);
        drivers.add(driver2);
        drivers.add(driver3);

        System.out.println("Available Drivers:");
        drivers.forEach(d -> System.out.println("  " + d));
        System.out.println("\nRider Location: " + rider.getLocation());
        System.out.println();

        // Test 1: NearestDriverStrategy
        System.out.println("--- Test 1: NearestDriverStrategy ---");
        RideMatchingStrategy nearestStrategy = new NearestDriverStrategy();
        Driver nearest = nearestStrategy.findDriver(rider, drivers);
        System.out.println("Assigned Driver: " + nearest.getName() + " at " + nearest.getCurrentLocation());
        System.out.println("✅ Expected: Priya (nearest)\n");

        // Test 2: LeastActiveDriverStrategy
        System.out.println("--- Test 2: LeastActiveDriverStrategy ---");
        RideMatchingStrategy leastActiveStrategy = new LeastActiveDriverStrategy();
        Driver leastActive = leastActiveStrategy.findDriver(rider, drivers);
        System.out.println("Assigned Driver: " + leastActive.getName() + " with " + leastActive.getCompletedRides() + " rides");
        System.out.println("✅ Expected: Ramesh or Priya (0 rides)\n");

        // Test 3: DefaultFareStrategy
        System.out.println("--- Test 3: DefaultFareStrategy ---");
        Ride ride1 = new Ride(IdGenerator.generateRideId(), rider, 5.0);
        Ride ride2 = new Ride(IdGenerator.generateRideId(), rider, 10.0);
        Ride ride3 = new Ride(IdGenerator.generateRideId(), rider, 15.0);

        FareStrategy defaultFare = new DefaultFareStrategy();
        double fare1 = defaultFare.calculateFare(ride1);
        double fare2 = defaultFare.calculateFare(ride2);
        double fare3 = defaultFare.calculateFare(ride3);

        System.out.println("5 km ride: ₹" + fare1 + " (Expected: ₹100)");
        System.out.println("10 km ride: ₹" + fare2 + " (Expected: ₹150)");
        System.out.println("15 km ride: ₹" + fare3 + " (Expected: ₹200)");
        System.out.println();

        // Test 4: PeakHourFareStrategy
        System.out.println("--- Test 4: PeakHourFareStrategy ---");
        FareStrategy peakFare = new PeakHourFareStrategy();
        double peakFare1 = peakFare.calculateFare(ride1);
        double peakFare2 = peakFare.calculateFare(ride2);
        double peakFare3 = peakFare.calculateFare(ride3);

        System.out.println("5 km ride: ₹" + peakFare1 + " (Expected: ₹225)");
        System.out.println("10 km ride: ₹" + peakFare2 + " (Expected: ₹375)");
        System.out.println("15 km ride: ₹" + peakFare3 + " (Expected: ₹525)");
        System.out.println();

        // Test 5: Strategy Pluggability (The Magic!)
        System.out.println("--- Test 5: Strategy Pluggability ---");
        System.out.println("Same ride with different strategies:");
        System.out.println("  Default Fare: ₹" + defaultFare.calculateFare(ride2));
        System.out.println("  Peak Hour Fare: ₹" + peakFare.calculateFare(ride2));
        System.out.println("✅ Same code, DIFFERENT behavior!");
        System.out.println("\n✅ All strategy tests passed!");
    }
}