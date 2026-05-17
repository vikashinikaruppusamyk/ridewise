package com.airtribe.ridewise;

import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.strategy.PeakHourFareStrategy;
import com.airtribe.ridewise.strategy.FareStrategy;
import org.junit.Test;
import static org.junit.Assert.*;

public class PeakHourFareStrategyTest {

    @Test
    public void testPeakHourFareCalculation() {
        FareStrategy strategy = new PeakHourFareStrategy();
        Rider rider = new Rider("R1", "Vikashini", "Indiranagar");
        Driver driver = new Driver("D1", "Ramesh", "Whitefield", true);
        Ride ride = new Ride("RIDE1", rider, 5.0);
        ride.setDriver(driver);

        double fare = strategy.calculateFare(ride);

        assertEquals(225.0, fare, 0.01);
    }

    @Test
    public void testPeakHourFare10km() {
        FareStrategy strategy = new PeakHourFareStrategy();
        Rider rider = new Rider("R1", "Vikashini", "Indiranagar");
        Driver driver = new Driver("D1", "Ramesh", "Whitefield", true);
        Ride ride = new Ride("RIDE1", rider, 10.0);
        ride.setDriver(driver);

        double fare = strategy.calculateFare(ride);

        assertEquals(375.0, fare, 0.01);
    }
}