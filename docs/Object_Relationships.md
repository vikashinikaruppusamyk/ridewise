# RideWise - Object Relationships & Design Patterns

## Entity Relationships

### 1. Rider → Ride (Association)

**Type**: One-to-Many Association
**Cardinality**: 1 Rider : Many Rides

┌──────────┐
│  Rider   │
└────┬─────┘
│ 1
│ requests
│ *
▼
┌──────────┐
│   Ride   │
└──────────┘

**Code:**
```java
public class Ride {
    private Rider rider;  // ← Rider associated with Ride
    
    public Ride(String id, Rider rider, double distance) {
        this.rider = rider;
    }
    
    public Rider getRider() {
        return rider;
    }
}
```

**Explanation:**
- A Rider can request multiple Rides
- Each Ride belongs to exactly one Rider
- Rider is stored as reference in Ride
- No bidirectional reference (clean design)

**Lifecycle:**

Rider requests Ride → RideService creates Ride with Rider reference
→ Ride status changes from REQUESTED → ASSIGNED
→ Driver is assigned
→ Ride is COMPLETED

---

### 2. Driver → Ride (Association)

**Type**: One-to-Many Association
**Cardinality**: 1 Driver : Many Rides

┌──────────┐
│  Driver  │
└────┬─────┘
│ 1
│ accepts
│ *
▼
┌──────────┐
│   Ride   │
└──────────┘

**Code:**
```java
public class Ride {
    private Driver driver;  // ← Driver associated with Ride (optional)
    
    public Driver getDriver() {
        return driver;  // null until assigned
    }
    
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
```

**Explanation:**
- Initially, Ride.driver is null (REQUESTED state)
- When driver is assigned, driver reference is set
- Each Ride can have at most one Driver
- Driver can accept multiple Rides

**Lifecycle:**

REQUESTED state: driver = null
↓
ASSIGNED state: driver = assigned Driver object
↓
COMPLETED state: driver = still has reference
↓
Driver stats updated: completedRides++

---

### 3. Ride → FareReceipt (Composition)

**Type**: Composition (Strong Ownership)
**Cardinality**: 1 Ride : 1 FareReceipt
┌──────────────────────┐
│      Ride            │
│  ┌────────────────┐  │
│  │  FareReceipt   │  │
│  │  (composed)    │  │
│  └────────────────┘  │
└──────────────────────┘

**Code:**
```java
public class Ride {
    private String id;
    private Rider rider;
    private Driver driver;
    private double distance;
    private RideStatus status;
    // No FareReceipt stored in Ride (created separately)
}

public class FareReceipt {
    private String rideId;      // ← Reference back to Ride ID
    private double amount;
    private LocalDateTime generatedAt;
}
```

**Why Composition?**
- FareReceipt is created AFTER Ride is completed
- FareReceipt represents the financial outcome of a Ride
- FareReceipt depends on Ride (rideId is required)
- Without Ride, FareReceipt doesn't make sense

**Lifecycle:**
Ride created (REQUESTED)
↓
Ride ASSIGNED to driver
↓
Ride COMPLETED
↓
FareReceipt created with rideId + amount
↓
FareReceipt stored separately (not in Ride object)

**Why NOT Reference in Ride?**
```java
// ❌ BAD Design:
public class Ride {
    private FareReceipt receipt;  // Created after ride completion
    // But Ride needs receipt only for final display
}

// ✅ GOOD Design:
// FareReceipt stored separately in RideService
private List<FareReceipt> receipts;
```

---

### 4. RideService → Strategies (Composition)

**Type**: Composition (Dependency Injection)
**Cardinality**: 1 RideService : 1 RideMatchingStrategy : 1 FareStrategy
┌────────────────────────────────────────┐
│  RideService                           │
│  ┌──────────────────────────────────┐  │
│  │  RideMatchingStrategy (injected) │  │
│  ├──────────────────────────────────┤  │
│  │  FareStrategy (injected)         │  │
│  └──────────────────────────────────┘  │
└────────────────────────────────────────┘

**Code:**
```java
public class RideService {
    private RideMatchingStrategy rideMatchingStrategy;
    private FareStrategy fareStrategy;
    
    // Constructor Injection
    public RideService(RideMatchingStrategy rideMatchingStrategy, 
                       FareStrategy fareStrategy) {
        this.rideMatchingStrategy = rideMatchingStrategy;
        this.fareStrategy = fareStrategy;
    }
    
    public void assignDriver(Ride ride, DriverService driverService) {
        Driver driver = rideMatchingStrategy.findDriver(
            ride.getRider(), 
            driverService.getAvailableDrivers()
        );
        // ...
    }
    
    public FareReceipt completedRide(Ride ride) {
        double fare = fareStrategy.calculateFare(ride);
        // ...
    }
}
```

**Composition Benefits:**
- Strategies are injected at construction time
- RideService doesn't create strategies (not responsible)
- Easy to swap strategies
- Easy to test with mock strategies

**Usage Example:**
```java
// ✅ Create service with different strategies
RideService defaultService = new RideService(
    new NearestDriverStrategy(),
    new DefaultFareStrategy()
);

RideService peakHourService = new RideService(
    new LeastActiveDriverStrategy(),
    new PeakHourFareStrategy()
);
// Same RideService code, different behavior!
```

---

## Design Pattern: Strategy Pattern

**Pattern Type**: Behavioral Pattern

**Problem Solved:**
- Need to change algorithm at runtime
- Want to avoid if-else chains for algorithm selection
- Need to test algorithms independently

**Solution:**
1. Define interface for algorithm family
2. Create concrete implementations
3. Inject into client (RideService)
4. Client uses interface, not concrete classes

**Diagram:**
┌────────────────────────┐
│ RideMatchingStrategy   │ (Interface)
│   + findDriver()       │
└────────┬───────────────┘
△
│ implements
┌────┴────────────────────────┐
│                             │
┌───────────────────┐  ┌──────────────────────┐
│ NearestDriver     │  │ LeastActiveDriver    │
│ Strategy          │  │ Strategy             │
│ + findDriver()    │  │ + findDriver()       │
└───────────────────┘  └──────────────────────┘
Uses hash-based         Uses completed
distance calc           rides count

**Implementation in RideWise:**
```java
// ✅ Interface
public interface RideMatchingStrategy {
    Driver findDriver(Rider rider, List<Driver> availableDrivers);
}

// ✅ Concrete Implementation 1
public class NearestDriverStrategy implements RideMatchingStrategy {
    @Override
    public Driver findDriver(Rider rider, List<Driver> availableDrivers) {
        // Algorithm: Find nearest driver
    }
}

// ✅ Concrete Implementation 2
public class LeastActiveDriverStrategy implements RideMatchingStrategy {
    @Override
    public Driver findDriver(Rider rider, List<Driver> availableDrivers) {
        // Algorithm: Find least active driver
    }
}

// ✅ Client uses strategy without knowing implementation
public class RideService {
    private RideMatchingStrategy strategy;
    
    public RideService(RideMatchingStrategy strategy, FareStrategy fareStrategy) {
        this.strategy = strategy;  // Injected!
    }
    
    public void assignDriver(Ride ride, DriverService driverService) {
        Driver driver = strategy.findDriver(ride.getRider(), 
                                           driverService.getAvailableDrivers());
    }
}
```

**Why Strategy Pattern?**
- ✅ Follows Open/Closed Principle
- ✅ Each strategy is independent and testable
- ✅ Easy to add new strategies
- ✅ Runtime selection of algorithm

---

## Data Flow & Relationships
┌─────────────────────────────────────────────────────────────┐
│                    RideWise System                          │
│                                                             │
│  ┌──────────────────┐                                       │
│  │   Rider          │                                       │
│  │  - id            │                                       │
│  │  - name          │                                       │
│  │  - location      │                                       │
│  └─────────┬────────┘                                       │
│            │ requests                                       │
│            ▼                                                │
│  ┌──────────────────┐      ┌──────────────────┐            │
│  │   Ride           │      │   Driver         │            │
│  │  - id            │◄─────┤  - id            │            │
│  │  - rider ─────────┐     │  - name          │            │
│  │  - driver ───────┐└─────┤  - location      │            │
│  │  - distance      │      │  - available     │            │
│  │  - status        │      │  - completedRides            │
│  └─────────┬────────┘      └──────────────────┘            │
│            │ creates                                       │
│            ▼                                                │
│  ┌──────────────────┐                                       │
│  │   FareReceipt    │                                       │
│  │  - rideId ───────┼──► (references Ride ID)             │
│  │  - amount        │                                       │
│  │  - generatedAt   │                                       │
│  └──────────────────┘                                       │
│                                                             │
│  Uses:                                                      │
│  - RideMatchingStrategy (NearestDriver / LeastActive)      │
│  - FareStrategy (DefaultFare / PeakHourFare)               │
└─────────────────────────────────────────────────────────────┘

---

## Key Design Decisions

| Decision | Reason | Benefit |
|----------|--------|---------|
| Rider stored as reference in Ride | Ride belongs to specific Rider | Single responsibility |
| Driver initially null, set later | Driver not known until matching | Supports ride lifecycle |
| FareReceipt separate from Ride | Created after ride completion | Composition, clean design |
| Strategies injected via constructor | Runtime algorithm selection | OCP, DIP, testability |
| No bidirectional references | Clean, simple design | Less memory, easier to understand |
| In-memory storage (ArrayList) | MVP, no persistence needed | Simple, fast for demo |

---

## Conclusion

RideWise demonstrates:
✅ Clear entity relationships following composition principles
✅ Strategy pattern for pluggable algorithms
✅ Proper use of interfaces for abstraction
✅ Well-designed object model supporting clean code
✅ Production-quality architecture suitable for interviews

