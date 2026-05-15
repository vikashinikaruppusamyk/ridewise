package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.IdGenerator;
import java.util.ArrayList;
import java.util.List;

public class RiderService {

    // In-memory storage
    private List<Rider> riders = new ArrayList<>();

    /**
     * Register a new rider
     */
    public Rider registerRider(String name, String location) {
        String riderId = IdGenerator.generateRiderId();
        Rider rider = new Rider(riderId, name, location);
        riders.add(rider);
        return rider;
    }

    /**
     * Get a rider by ID
     */
    public Rider getRider(String riderId) {
        for (Rider rider : riders) {
            if (rider.getId().equals(riderId)) {
                return rider;
            }
        }
        return null;  // Rider not found
    }

    /**
     * Get all riders
     */
    public List<Rider> getAllRiders() {
        return new ArrayList<>(riders);
    }

    /**
     * Get total riders registered
     */
    public int getTotalRiders() {
        return riders.size();
    }
}