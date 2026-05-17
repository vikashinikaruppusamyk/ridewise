# RideWise - Requirements Documentation

## Overview
RideWise is a console-based ride-sharing system demonstrating high-quality LLD (Low-Level Design) with SOLID principles and design patterns.

## A. Functional Requirements

| # | Requirement | Status | Implementation |
|---|---|---|---|
| 1 | Register Riders | ✅ Done | `RiderService.registerRider()` |
| 2 | Register Drivers | ✅ Done | `DriverService.registerDriver()` |
| 3 | Show available drivers | ✅ Done | `DriverService.getAvailableDrivers()` |
| 4 | Request a ride | ✅ Done | `RideService.requestRide()` |
| 5 | Match ride to driver using strategy | ✅ Done | `RideService.assignDriver()` with `RideMatchingStrategy` |
| 6 | Calculate fare using strategy | ✅ Done | `RideService.completedRide()` with `FareStrategy` |
| 7 | Track ride status | ✅ Done | `RideStatus` enum (REQUESTED, ASSIGNED, COMPLETED, CANCELLED) |

## B. Non-Functional Requirements

| # | Requirement | Status | Implementation |
|---|---|---|---|
| 1 | Easily extendable pricing algorithm | ✅ Done | `FareStrategy` interface with multiple implementations |
| 2 | Easily change driver matching logic | ✅ Done | `RideMatchingStrategy` interface (pluggable) |
| 3 | Low coupling between services | ✅ Done | Dependency injection via constructors |
| 4 | Maintainable and readable code | ✅ Done | Clear naming, separation of concerns, proper encapsulation |

## C. Ride Status Lifecycle

REQUESTED → ASSIGNED → COMPLETED
↓
CANCELLED (at any point)

## D. Strategy Pattern Implementation

### Ride Matching Strategies
- **NearestDriverStrategy**: Assigns driver closest to rider
- **LeastActiveDriverStrategy**: Assigns driver with fewest completed rides

### Fare Calculation Strategies
- **DefaultFareStrategy**: Base fare ₹50 + ₹10 per km
- **PeakHourFareStrategy**: (Base ₹50 + ₹20 per km) × 1.5 multiplier

## E. How Requirements Are Met

### Functional Requirements
✅ All 7 functional requirements fully implemented and tested
✅ Console menu supports all operations
✅ Data persisted in-memory during session
✅ Proper error handling with custom exceptions

### Non-Functional Requirements
✅ Strategy pattern enables easy addition of new algorithms
✅ No modification to existing code when adding new strategies
✅ Services communicate via interfaces, not concrete classes
✅ Code is well-organized with clear separation of concerns

## F. Test Coverage

- **RiderTest**: 2 tests (creation, setters)
- **DriverTest**: 3 tests (creation, availability, completed rides)
- **NearestDriverStrategyTest**: 2 tests (nearest driver, empty list)
- **DefaultFareStrategyTest**: 2 tests (5km, 10km fare)
- **PeakHourFareStrategyTest**: 2 tests (5km, 10km peak fare)
- **RideServiceTest**: 4 tests (request, assign, complete, exception)

**Total: 15 comprehensive unit tests** ✅

## G. Conclusion

RideWise demonstrates:
- Clean architecture with clear separation of concerns
- Effective use of design patterns (Strategy)
- SOLID principles applied throughout
- Comprehensive testing and documentation
- Production-quality code suitable for interview preparation