package com.airtribe.ridewise;

import com.airtribe.ridewise.model.Rider;
import org.junit.Test;
import static org.junit.Assert.*;

public class RiderTest {

    @Test
    public void testRiderCreation() {
        // Arrange
        String riderId = "RIDER_1001";
        String name = "Vikashini";
        String location = "Indiranagar";

        // Act
        Rider rider = new Rider(riderId, name, location);

        // Assert
        assertEquals(riderId, rider.getId());
        assertEquals(name, rider.getName());
        assertEquals(location, rider.getLocation());
    }

    @Test
    public void testRiderSetters() {
        // Arrange
        Rider rider = new Rider("R1", "John", "Location1");

        // Act
        rider.setName("Jane");
        rider.setLocation("Location2");

        // Assert
        assertEquals("Jane", rider.getName());
        assertEquals("Location2", rider.getLocation());
    }
}