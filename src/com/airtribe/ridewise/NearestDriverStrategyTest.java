package com.airtribe.ridewise;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class NearestDriverStrategyTest {

    @Test
    public void testFindNearestDriver() {
        RideMatchingStrategy strategy = new NearestDriverStrategy();
        Rider rider = new Rider("R1", "Vikashini", "Indiranagar");

        List<Driver> drivers = new ArrayList<>();
        Driver driver1 = new Driver("D1", "Ramesh", "Marathahalli", true);
        Driver driver2 = new Driver("D2", "Priya", "Indiranagar", true);
        Driver driver3 = new Driver("D3", "Anil", "Whitefield", true);

        drivers.add(driver1);
        drivers.add(driver2);
        drivers.add(driver3);

        Driver nearestDriver = strategy.findDriver(rider, drivers);

        assertNotNull(nearestDriver);
        assertEquals("D2", nearestDriver.getId());
    }

    @Test
    public void testFindDriverWithEmptyList() {
        RideMatchingStrategy strategy = new NearestDriverStrategy();
        Rider rider = new Rider("R1", "Vikashini", "Indiranagar");
        List<Driver> drivers = new ArrayList<>();

        Driver result = strategy.findDriver(rider, drivers);

        assertNull(result);
    }
}