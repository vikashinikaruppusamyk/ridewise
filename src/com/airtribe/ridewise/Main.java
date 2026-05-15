package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static RiderService riderService = new RiderService();
    private static DriverService driverService = new DriverService();
    private static RideService rideService = new RideService(
            new NearestDriverStrategy(),
            new DefaultFareStrategy()
    );

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   Welcome to RideWise 🚗              ║");
        System.out.println("║   Ride-Sharing System                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice (1-7): ");

            switch (choice) {
                case 1:
                    addRider();
                    break;
                case 2:
                    addDriver();
                    break;
                case 3:
                    viewAvailableDrivers();
                    break;
                case 4:
                    requestRide();
                    break;
                case 5:
                    completeRide();
                    break;
                case 6:
                    viewAllRides();
                    break;
                case 7:
                    running = false;
                    System.out.println("\n👋 Thank you for using RideWise! Goodbye!\n");
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please enter a number between 1 and 7.\n");
            }
        }
    }

    /**
     * Display main menu
     */
    private static void displayMenu() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          MAIN MENU                     ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Add Rider                          ║");
        System.out.println("║  2. Add Driver                         ║");
        System.out.println("║  3. View Available Drivers             ║");
        System.out.println("║  4. Request Ride                       ║");
        System.out.println("║  5. Complete Ride                      ║");
        System.out.println("║  6. View All Rides                     ║");
        System.out.println("║  7. Exit                               ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    /**
     * Option 1: Add a new rider
     */
    private static void addRider() {
        System.out.println("\n--- Add New Rider ---");
        System.out.print("Enter rider name: ");
        String name = scanner.nextLine();

        System.out.print("Enter location: ");
        String location = scanner.nextLine();

        Rider rider = riderService.registerRider(name, location);
        System.out.println("✅ Rider registered successfully!");
        System.out.println("   ID: " + rider.getId());
        System.out.println("   Name: " + rider.getName());
        System.out.println("   Location: " + rider.getLocation() + "\n");
    }

    /**
     * Option 2: Add a new driver
     */
    private static void addDriver() {
        System.out.println("\n--- Add New Driver ---");
        System.out.print("Enter driver name: ");
        String name = scanner.nextLine();

        System.out.print("Enter current location: ");
        String location = scanner.nextLine();

        Driver driver = driverService.registerDriver(name, location);
        System.out.println("✅ Driver registered successfully!");
        System.out.println("   ID: " + driver.getId());
        System.out.println("   Name: " + driver.getName());
        System.out.println("   Location: " + driver.getCurrentLocation());
        System.out.println("   Status: Available\n");
    }

    /**
     * Option 3: View all available drivers
     */
    private static void viewAvailableDrivers() {
        System.out.println("\n--- Available Drivers ---");
        List<Driver> availableDrivers = driverService.getAvailableDrivers();

        if (availableDrivers.isEmpty()) {
            System.out.println("❌ No drivers available right now.\n");
            return;
        }

        System.out.println("Total available: " + availableDrivers.size() + "\n");
        for (int i = 0; i < availableDrivers.size(); i++) {
            Driver driver = availableDrivers.get(i);
            System.out.println((i + 1) + ". " + driver.getName());
            System.out.println("   ID: " + driver.getId());
            System.out.println("   Location: " + driver.getCurrentLocation());
            System.out.println("   Completed Rides: " + driver.getCompletedRides() + "\n");
        }
    }

    /**
     * Option 4: Request a ride
     */
    private static void requestRide() {
        System.out.println("\n--- Request a Ride ---");

        // Step 1: Get rider ID
        System.out.print("Enter your rider ID: ");
        String riderId = scanner.nextLine();

        Rider rider = riderService.getRider(riderId);
        if (rider == null) {
            System.out.println("❌ Rider not found! Please register first.\n");
            return;
        }

        System.out.println("✅ Rider found: " + rider.getName());

        // Step 2: Get distance
        System.out.print("Enter distance (in km): ");
        double distance = getDoubleInput();

        // Step 3: Request ride
        Ride ride = rideService.requestRide(rider, distance);
        System.out.println("✅ Ride requested successfully!");
        System.out.println("   Ride ID: " + ride.getId());

        // Step 4: Assign driver
        try {
            rideService.assignDriver(ride, driverService);
            System.out.println("✅ Driver assigned: " + ride.getDriver().getName());

            // Step 5: Calculate fare
            var receipt = rideService.completedRide(ride);
            if (receipt != null) {
                System.out.println("✅ Ride completed!");
                System.out.println("   Distance: " + distance + " km");
                System.out.println("   Driver: " + ride.getDriver().getName());
                System.out.println("   Fare: ₹" + receipt.getAmount());
            }
            System.out.println();

        } catch (NoDriverAvailableException e) {
            System.out.println("❌ " + e.getMessage() + "\n");
        }
    }

    /**
     * Option 5: Complete a ride
     */
    private static void completeRide() {
        System.out.println("\n--- Complete a Ride ---");
        System.out.print("Enter ride ID: ");
        String rideId = scanner.nextLine();

        Ride ride = rideService.getRide(rideId);
        if (ride == null) {
            System.out.println("❌ Ride not found!\n");
            return;
        }

        if (ride.getDriver() == null) {
            System.out.println("❌ No driver assigned to this ride!\n");
            return;
        }

        var receipt = rideService.completedRide(ride);
        if (receipt != null) {
            System.out.println("✅ Ride completed successfully!");
            System.out.println("   Ride ID: " + ride.getId());
            System.out.println("   Driver: " + ride.getDriver().getName());
            System.out.println("   Fare: ₹" + receipt.getAmount() + "\n");
        }
    }

    /**
     * Option 6: View all rides
     */
    private static void viewAllRides() {
        System.out.println("\n--- All Rides ---");
        List<Ride> allRides = rideService.getAllRides();

        if (allRides.isEmpty()) {
            System.out.println("❌ No rides yet.\n");
            return;
        }

        System.out.println("Total rides: " + allRides.size() + "\n");
        for (int i = 0; i < allRides.size(); i++) {
            Ride ride = allRides.get(i);
            System.out.println((i + 1) + ". Ride ID: " + ride.getId());
            System.out.println("   Rider: " + ride.getRider().getName());
            System.out.println("   Driver: " + (ride.getDriver() != null ? ride.getDriver().getName() : "Not assigned"));
            System.out.println("   Distance: " + ride.getDistance() + " km");
            System.out.println("   Status: " + ride.getStatus() + "\n");
        }
    }

    /**
     * Helper: Get integer input
     */
    private static int getIntInput(String prompt) {
        try {
            System.out.print(prompt);
            int input = Integer.parseInt(scanner.nextLine());
            return input;
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a number.\n");
            return getIntInput(prompt);
        }
    }

    /**
     * Helper: Get double input
     */
    private static double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a valid number.\n");
            System.out.print("Enter distance (in km): ");
            return getDoubleInput();
        }
    }
}