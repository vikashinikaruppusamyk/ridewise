package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

public class PeakHourFareStrategy implements FareStrategy {

    private static final double BASE_RATE = 50.0;           // Base fare
    private static final double PER_KM_RATE = 20.0;         // 2x normal rate
    private static final double SURGE_MULTIPLIER = 1.5;     // 50% surge

    @Override
    public double calculateFare(Ride ride) {
        double distance = ride.getDistance();
        double baseFare = BASE_RATE + (distance * PER_KM_RATE);
        return baseFare * SURGE_MULTIPLIER;
    }
}