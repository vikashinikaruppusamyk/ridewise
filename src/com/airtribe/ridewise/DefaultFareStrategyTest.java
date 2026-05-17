package com.airtribe.ridewise;

import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.FareStrategy;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultFareStrategyTest {

    @Test
    public void testDefaultFareCalculation() {
        FareStrategy strategy = new DefaultFareStrategy();
        Rider rider = new Rider("R1", "Vikashini", "Indiranagar");
        Driver driver = new Driver("D1", "Ramesh", "Whitefield", true);
        Ride ride = new Ride("RIDE1", rider, 5.0);
        ride.setDriver(driver);

        double fare = strategy.calculateFare(ride);

        assertEquals(100.0, fare, 0.01);
    }

    @Test
    public void testDefaultFare10km() {
        FareStrategy strategy = new DefaultFareStrategy();
        Rider rider = new Rider("R1", "Vikashini", "Indiranagar");
        Driver driver = new Driver("D1", "Ramesh", "Whitefield", true);
        Ride ride = new Ride("RIDE1", rider, 10.0);
        ride.setDriver(driver);

        double fare = strategy.calculateFare(ride);

        assertEquals(150.0, fare, 0.01);
    }
}