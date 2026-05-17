# RideWise - SOLID Principles Reflection

## S - Single Responsibility Principle

**Definition**: A class should have only one reason to change.

### Implementation in RideWise

**RiderService**: Only manages rider data
```java
public class RiderService {
    private List<Rider> riders = new ArrayList<>();
    
    public Rider registerRider(String name, String location) { ... }
    public Rider getRider(String riderId) { ... }
    public List<Rider> getAllRiders() { ... }
}
```
✅ **Single Responsibility**: Handles only rider registration and retrieval

**DriverService**: Only manages driver data
```java
public class DriverService {
    private List<Driver> drivers = new ArrayList<>();
    
    public Driver registerDriver(String name, String currentLocation) { ... }
    public Driver getDriver(String driverId) { ... }
    public List<Driver> getAvailableDrivers() { ... }
}
```
✅ **Single Responsibility**: Handles only driver registration and availability

**RideService**: Only orchestrates rides
```java
public class RideService {
    private RideMatchingStrategy rideMatchingStrategy;
    private FareStrategy fareStrategy;
    
    public void assignDriver(Ride ride, DriverService driverService) { ... }
    public FareReceipt completedRide(Ride ride) { ... }
}
```
✅ **Single Responsibility**: Handles only ride lifecycle

### Why SRP Matters
- Easy to test each service independently
- Changes to rider logic don't affect driver logic
- Clear separation of concerns

---

## O - Open/Closed Principle

**Definition**: Classes should be open for extension, closed for modification.

### Implementation in RideWise

**Strategy Pattern enables OCP:**

Instead of modifying RideService to add new fare calculation:
```java
// ❌ BAD: Modify existing code
public double calculateFare(Ride ride, String type) {
    if (type.equals("default")) {
        return 50 + (ride.getDistance() * 10);
    } else if (type.equals("peakHour")) {
        return (50 + ride.getDistance() * 20) * 1.5;
    }
    // Adding new type requires modifying this method!
}
```

✅ **GOOD: Create new implementation without modifying existing code**
```java
// FareStrategy interface (closed for modification)
public interface FareStrategy {
    double calculateFare(Ride ride);
}

// DefaultFareStrategy (existing)
public class DefaultFareStrategy implements FareStrategy {
    public double calculateFare(Ride ride) {
        return 50 + (ride.getDistance() * 10);
    }
}

// PeakHourFareStrategy (existing)
public class PeakHourFareStrategy implements FareStrategy {
    public double calculateFare(Ride ride) {
        return (50 + ride.getDistance() * 20) * 1.5;
    }
}

// NEW: EarlyBirdFareStrategy (add without modifying existing code!)
public class EarlyBirdFareStrategy implements FareStrategy {
    public double calculateFare(Ride ride) {
        return (50 + ride.getDistance() * 10) * 0.8; // 20% discount
    }
}
```

**RideService remains unchanged:**
```java
public RideService(RideMatchingStrategy rideMatchingStrategy, 
                   FareStrategy fareStrategy) {
    this.rideMatchingStrategy = rideMatchingStrategy;
    this.fareStrategy = fareStrategy; // Works with ANY FareStrategy!
}
```

### Why OCP Matters
- Add new features without touching existing code
- Reduces bugs from modifying tested code
- Strategies are pluggable and interchangeable

---

## L - Liskov Substitution Principle

**Definition**: Subtypes must be substitutable for their base types.

### Implementation in RideWise

**Any RideMatchingStrategy can replace another:**
```java
// Both work seamlessly
RideService service1 = new RideService(
    new NearestDriverStrategy(),  // ← Different strategy
    new DefaultFareStrategy()
);

RideService service2 = new RideService(
    new LeastActiveDriverStrategy(),  // ← Different strategy
    new DefaultFareStrategy()
);

// Both produce valid Ride objects without client knowing difference
Ride ride1 = service1.requestRide(rider, 10.0);
Ride ride2 = service2.requestRide(rider, 10.0);
```

**Any FareStrategy can replace another:**
```java
RideService peakHourService = new RideService(
    new NearestDriverStrategy(),
    new PeakHourFareStrategy()  // ← Can swap for DefaultFareStrategy
);

FareReceipt receipt = peakHourService.completedRide(ride);
// Works correctly regardless of strategy implementation
```

### Why LSP Matters
- Predictable behavior regardless of implementation
- Tests pass for all implementations
- Easy to swap implementations at runtime

---

## I - Interface Segregation Principle

**Definition**: Clients should depend on small, focused interfaces.

### Implementation in RideWise

**RideMatchingStrategy** - Single method interface
```java
public interface RideMatchingStrategy {
    Driver findDriver(Rider rider, List<Driver> availableDrivers);
}
```
✅ **Focused**: Only one responsibility - finding a driver

**FareStrategy** - Single method interface
```java
public interface FareStrategy {
    double calculateFare(Ride ride);
}
```
✅ **Focused**: Only one responsibility - calculating fare

### Why NOT This (Violation of ISP):
```java
// ❌ BAD: Too many methods, clients don't need all
public interface RideService {
    Ride requestRide(Rider rider, double distance);
    void assignDriver(Ride ride, DriverService driverService);
    FareReceipt completedRide(Ride ride);
    void cancelRide(Ride ride);
    List<Ride> getAllRides();
    // ... 20 more methods
}
```

### Why ISP Matters
- Classes only implement what they need
- Easier to create mock implementations for testing
- Clear contracts between classes

---

## D - Dependency Inversion Principle

**Definition**: Depend on abstractions, not concrete implementations.

### Implementation in RideWise

**RideService depends on interfaces (abstractions):**
```java
public class RideService {
    // ✅ Depend on interface, not concrete class
    private RideMatchingStrategy rideMatchingStrategy;
    private FareStrategy fareStrategy;
    
    // ✅ Constructor injection
    public RideService(RideMatchingStrategy rideMatchingStrategy, 
                       FareStrategy fareStrategy) {
        this.rideMatchingStrategy = rideMatchingStrategy;
        this.fareStrategy = fareStrategy;
    }
}
```

### Why NOT This (Violation of DIP):
```java
// ❌ BAD: Depends on concrete class, hard to change
public class RideService {
    private NearestDriverStrategy strategy;  // Concrete class!
    
    public RideService() {
        this.strategy = new NearestDriverStrategy();  // Hard-coded!
    }
}
```

**Problems with this approach:**
- Can't use LeastActiveDriverStrategy without modifying code
- Can't mock strategies for testing
- Tightly coupled to specific implementation

### Our Approach (DIP Compliance):
```java
// ✅ GOOD: Depends on interface
RideService service = new RideService(
    new LeastActiveDriverStrategy(),  // Can swap easily
    new PeakHourFareStrategy()         // Can swap easily
);
```

### Why DIP Matters
- Easy to swap implementations
- Perfect for unit testing (inject mock strategies)
- Decoupled architecture
- Flexible and extensible

---

## Summary: How RideWise Demonstrates SOLID

| Principle | How Applied | Benefit |
|-----------|-------------|---------|
| **SRP** | Each service has single responsibility | Easy to test, maintain, and modify |
| **OCP** | Strategy pattern allows new strategies without modifying RideService | Add features without touching existing code |
| **LSP** | All strategy implementations are interchangeable | Predictable, reliable behavior |
| **ISP** | Small, focused interfaces (RideMatchingStrategy, FareStrategy) | Clients only depend on what they need |
| **DIP** | RideService depends on interfaces, not concrete classes | Flexible, testable, loosely coupled |

## Interview Talking Points

✅ **"I used Strategy Pattern to demonstrate OCP"** - Open for extension (new strategies) but closed for modification

✅ **"Dependency Injection shows DIP"** - Services depend on interfaces injected via constructor

✅ **"Each service has single responsibility"** - RiderService, DriverService, RideService each do one thing

✅ **"Small focused interfaces"** - RideMatchingStrategy and FareStrategy only define what's necessary

✅ **"Easy to test"** - Can inject mock strategies for unit testing

## Conclusion

RideWise demonstrates production-quality understanding of SOLID principles. The architecture is:
- ✅ Maintainable (clear responsibilities)
- ✅ Extensible (new strategies without modification)
- ✅ Testable (dependency injection)
- ✅ Flexible (pluggable implementations)
- ✅ Professional (interview-ready)