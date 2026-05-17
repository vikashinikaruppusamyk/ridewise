# RideWise - Class Model & Architecture

## Package Structure

com/airtribe/ridewise/
├── model/                              # Domain entities
│   ├── Rider.java
│   ├── Driver.java
│   ├── Ride.java
│   ├── FareReceipt.java
│   ├── RideStatus.java
│   └── VehicleType.java
├── strategy/                           # Pluggable algorithms
│   ├── RideMatchingStrategy.java (interface)
│   ├── NearestDriverStrategy.java
│   ├── LeastActiveDriverStrategy.java
│   ├── FareStrategy.java (interface)
│   ├── DefaultFareStrategy.java
│   └── PeakHourFareStrategy.java
├── service/                            # Business logic
│   ├── RiderService.java
│   ├── DriverService.java
│   └── RideService.java
├── exception/                          # Custom exceptions
│   └── NoDriverAvailableException.java
├── util/                               # Utilities
│   └── IdGenerator.java
├── Main.java                           # Console UI / Entry point
│
└── Test Files (Unit Tests)             # JUnit Tests
├── RiderTest.java
├── DriverTest.java
├── NearestDriverStrategyTest.java
├── DefaultFareStrategyTest.java
├── PeakHourFareStrategyTest.java
└── RideServiceTest.java

## Model Classes

### Rider
```java
- id: String (unique identifier)
- name: String
- location: String
+ getId(): String
+ getName(): String
+ setName(String): void
+ getLocation(): String
+ setLocation(String): void
+ toString(): String
```

### Driver
```java
- id: String (unique identifier)
- name: String
- currentLocation: String
- available: boolean
- completedRides: int
+ getId(): String
+ getName(): String
+ getCurrentLocation(): String
+ setCurrentLocation(String): void
+ isAvailable(): boolean
+ setAvailable(boolean): void
+ getCompletedRides(): int
+ incrementCompletedRides(): void
+ toString(): String
```

### Ride
```java
- id: String (unique identifier)
- rider: Rider (composition)
- driver: Driver (optional, null initially)
- distance: double (in km)
- status: RideStatus (enum)
+ getId(): String
+ getRider(): Rider
+ getDriver(): Driver
+ setDriver(Driver): void
+ getDistance(): double
+ getStatus(): RideStatus
+ setStatus(RideStatus): void
+ toString(): String
```

### FareReceipt
```java
- rideId: String
- amount: double (in rupees)
- generatedAt: LocalDateTime
+ getRideId(): String
+ getAmount(): double
+ getGeneratedAt(): LocalDateTime
+ toString(): String
```

### RideStatus (Enum)

REQUESTED    → Ride requested, waiting for driver
ASSIGNED     → Driver assigned to ride
COMPLETED    → Ride completed, fare calculated
CANCELLED    → Ride cancelled

### VehicleType (Enum)

BIKE         → Two-wheeler
AUTO         → Auto-rickshaw
CAR          → Four-wheeler

## Strategy Interfaces

### RideMatchingStrategy
```java
public interface RideMatchingStrategy {
    Driver findDriver(Rider rider, List<Driver> availableDrivers);
}
```

**Implementations:**
- `NearestDriverStrategy`: Uses hash-based distance calculation
- `LeastActiveDriverStrategy`: Selects driver with minimum completed rides

### FareStrategy
```java
public interface FareStrategy {
    double calculateFare(Ride ride);
}
```

**Implementations:**
- `DefaultFareStrategy`: ₹50 + (distance × ₹10)
- `PeakHourFareStrategy`: (₹50 + distance × ₹20) × 1.5

## Service Layer

### RiderService
```java
- riders: List<Rider> (in-memory storage)
+ registerRider(name, location): Rider
+ getRider(riderId): Rider
+ getAllRiders(): List<Rider>
+ getTotalRiders(): int
```

### DriverService
```java
- drivers: List<Driver> (in-memory storage)
+ registerDriver(name, location): Driver
+ getDriver(driverId): Driver
+ getAvailableDrivers(): List<Driver>
+ updateDriverAvailability(driverId, available): void
+ getAllDrivers(): List<Driver>
+ getTotalDrivers(): int
```

### RideService
```java
- rides: List<Ride> (in-memory storage)
- receipts: List<FareReceipt> (in-memory storage)
- rideMatchingStrategy: RideMatchingStrategy (injected)
- fareStrategy: FareStrategy (injected)

+ RideService(strategy, fareStrategy) // Constructor injection
+ requestRide(rider, distance): Ride
+ assignDriver(ride, driverService): void
+ completedRide(ride): FareReceipt
+ cancelRide(ride): void
+ getRide(rideId): Ride
+ getAllRides(): List<Ride>
+ getReceipt(rideId): FareReceipt
+ getTotalRides(): int
```

## Utility Classes

### IdGenerator
```java
- riderCounter: AtomicInteger
- driverCounter: AtomicInteger
- rideCounter: AtomicInteger

+ generateRiderId(): String    (format: RIDER_1001, RIDER_1002, ...)
+ generateDriverId(): String   (format: DRIVER_2001, DRIVER_2002, ...)
+ generateRideId(): String     (format: RIDE_5001, RIDE_5002, ...)
```

## Exception Classes

### NoDriverAvailableException
```java
extends Exception
- message: String (descriptive error message)
+ NoDriverAvailableException(message)
+ getMessage(): String
```

## Main Application

### Main.java
```java
+ main(args): void
- displayMenu(): void
- addRider(): void
- addDriver(): void
- viewAvailableDrivers(): void
- requestRide(): void
- completeRide(): void
- viewAllRides(): void
- getIntInput(prompt): int
- getDoubleInput(): double
```

**Menu Options:**
1. Add Rider
2. Add Driver
3. View Available Drivers
4. Request Ride
5. Complete Ride
6. View Rides
7. Exit

## Data Flow

User Input (Main.java)
↓
Services (RiderService, DriverService, RideService)
↓
Models (Rider, Driver, Ride, FareReceipt)
↓
Strategies (RideMatchingStrategy, FareStrategy)
↓
In-Memory Storage (ArrayList)
↓
Console Output

## Design Highlights

✅ **Separation of Concerns**: Each class has single responsibility
✅ **Dependency Injection**: Strategies injected via constructor
✅ **Interface-Based Design**: Services depend on interfaces
✅ **Composition Over Inheritance**: No class hierarchy, only composition
✅ **Immutable IDs**: Generated once, never changed
✅ **Clean Naming**: Self-documenting code
