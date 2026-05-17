package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RideServiceTest {

    private RideService rideService;
    private RiderService riderService;
    private DriverService driverService;
    private Rider testRider;
    private Driver testDriver;

    @Before
    public void setUp() {
        rideService = new RideService(new NearestDriverStrategy(), new DefaultFareStrategy());
        riderService = new RiderService();
        driverService = new DriverService();

        testRider = riderService.registerRider("Vikashini", "Indiranagar");
        testDriver = driverService.registerDriver("Ramesh", "Indiranagar");
    }

    @Test
    public void testRequestRide() {
        Ride ride = rideService.requestRide(testRider, 10.0);

        assertNotNull(ride);
        assertEquals(RideStatus.REQUESTED, ride.getStatus());
        assertEquals(testRider.getId(), ride.getRider().getId());
        assertEquals(10.0, ride.getDistance(), 0.01);
    }

    @Test
    public void testAssignDriver() throws NoDriverAvailableException {
        Ride ride = rideService.requestRide(testRider, 10.0);

        rideService.assignDriver(ride, driverService);

        assertNotNull(ride.getDriver());
        assertEquals(RideStatus.ASSIGNED, ride.getStatus());
        assertFalse(ride.getDriver().isAvailable());
    }

    @Test
    public void testCompleteRide() throws NoDriverAvailableException {
        Ride ride = rideService.requestRide(testRider, 10.0);
        rideService.assignDriver(ride, driverService);

        var receipt = rideService.completedRide(ride);

        assertNotNull(receipt);
        assertEquals(RideStatus.COMPLETED, ride.getStatus());
        assertEquals(150.0, receipt.getAmount(), 0.01);
        assertTrue(ride.getDriver().isAvailable());
        assertEquals(1, ride.getDriver().getCompletedRides());
    }

    @Test
    public void testNoDriverAvailableException() {
        Ride ride = rideService.requestRide(testRider, 10.0);
        driverService.updateDriverAvailability(testDriver.getId(), false);

        try {
            rideService.assignDriver(ride, driverService);
            fail("Should have thrown NoDriverAvailableException");
        } catch (NoDriverAvailableException e) {
            assertTrue(e.getMessage().contains("No drivers available"));
        }
    }
}
