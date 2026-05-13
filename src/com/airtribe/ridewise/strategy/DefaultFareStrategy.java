package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

public class DefaultFareStrategy implements FareStrategy {

    private static final double BASE_RATE = 50.0;      // Base fare in rupees
    private static final double PER_KM_RATE = 10.0;    // Rate per km

    @Override
    public double calculateFare(Ride ride) {
        double distance = ride.getDistance();
        return BASE_RATE + (distance * PER_KM_RATE);
    }
}