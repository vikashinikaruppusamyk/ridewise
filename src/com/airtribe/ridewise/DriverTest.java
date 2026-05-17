package com.airtribe.ridewise;

import com.airtribe.ridewise.model.Driver;
import org.junit.Test;
import static org.junit.Assert.*;

public class DriverTest {

    @Test
    public void testDriverCreation() {
        String driverId = "DRIVER_2001";
        String name = "Ramesh";
        String location = "Whitefield";

        Driver driver = new Driver(driverId, name, location, true);

        assertEquals(driverId, driver.getId());
        assertEquals(name, driver.getName());
        assertEquals(location, driver.getCurrentLocation());
        assertTrue(driver.isAvailable());
        assertEquals(0, driver.getCompletedRides());
    }

    @Test
    public void testDriverAvailability() {
        Driver driver = new Driver("D1", "Ramesh", "Whitefield", true);
        driver.setAvailable(false);
        assertFalse(driver.isAvailable());
    }

    @Test
    public void testIncrementCompletedRides() {
        Driver driver = new Driver("D1", "Ramesh", "Whitefield", true);
        assertEquals(0, driver.getCompletedRides());

        driver.incrementCompletedRides();
        driver.incrementCompletedRides();

        assertEquals(2, driver.getCompletedRides());
    }
}