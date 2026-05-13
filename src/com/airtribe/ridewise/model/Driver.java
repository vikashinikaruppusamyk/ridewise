package com.airtribe.ridewise.model;

public class Driver {
    private String id;
    private String name;
    private String currentLocation;
    private boolean available;
    private int completedRides;

    public Driver(String id, String name, String currentLocation, boolean available) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.available = available;
        this.completedRides = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getCompletedRides() {
        return completedRides;
    }

    public void incrementCompletedRides() {
        this.completedRides++;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", currentLocation='" + currentLocation + '\'' +
                ", available=" + available +
                ", completedRides=" + completedRides +
                '}';
    }
}