# Technical University of Moldova
**Faculty of Computers, Informatics and Microelectronics**

## Laboratory Work No. 1

**Subject:** Tehnici și Mecanisme de Proiectare Software (TMPS)

**Project:** Online Taxi Application

**Mentor:** asist. univ. Istrati Daniel

**Student:** Cuibari Vladislav

**Group:** FAF-243

---

## Table of Contents

1. Introduction and Objectives
2. Application Description
3. Project Architecture
4. Implementation Details
5. SOLID Principles
6. Testing and Validation
7. Limitations and Future Development
8. Conclusion

---

## 1. Introduction and Objectives

This laboratory work presents the implementation of a simple online taxi application written in Java. The project is a foundation that will be extended throughout the semester with new functionality and software design patterns.

The application models a basic ride-hailing process. A passenger requests a ride, the system searches for available drivers and selects the nearest one, the fare is calculated using a selected tariff, a ride is created, and both the passenger and the driver are notified.

The main purpose is not to reproduce a complete commercial platform such as Uber or Bolt. Instead, the project shows how a small program can be divided into focused and extensible components by applying three SOLID principles:

* Single Responsibility Principle;
* Open/Closed Principle;
* Dependency Inversion Principle.

The implementation intentionally avoids functionality such as real payment processing, authentication, maps, GPS and networking. This keeps the first version understandable while leaving room for future development.

**Purpose.** The purpose of this laboratory work is to study and apply object-oriented design principles in a practical Java application: how responsibilities can be separated between classes, how new behavior can be added without modifying stable business logic, and how high-level services can depend on abstractions instead of concrete implementations.

**Objectives:**

1. Create a small online-taxi domain using Java classes.
2. Separate pricing, driver matching, storage, notification and ride-coordination responsibilities.
3. Implement three SOLID principles.
4. Make the fare calculation extensible.
5. Separate driver storage from the ride logic.
6. Handle the case when no driver is available.
7. Keep the application simple enough to understand and demonstrate.
8. Prepare the architecture for future semester requirements.

---

## 2. Application Description

The application simulates the following scenario:

1. Several drivers are created and saved in the repository, each with a name, a car model and a location.
2. A passenger is created.
3. The passenger requests a ride by specifying a pickup location and a destination.
4. The ride service retrieves the list of available drivers.
5. The driver matcher selects the driver closest to the pickup location.
6. The selected driver is marked as unavailable.
7. The fare is calculated from the trip distance using the selected tariff (standard, night or premium).
8. A new `Ride` with a unique identifier is created.
9. The passenger and the driver are notified.
10. When the ride is completed, its status changes to `COMPLETED` and the driver becomes available again.

If no driver is available, the passenger is informed and no ride is created.

---

## 3. Project Architecture

The project is split into packages, each responsible for one part of the system.

```
src/taxi/
├── Main.java
├── model/          Location, Passenger, Driver, Ride, RideStatus
├── pricing/        FareCalculator (interface), StandardFareCalculator,
│                   NightFareCalculator, PremiumFareCalculator
├── repository/     DriverRepository (interface), InMemoryDriverRepository
├── matching/       DriverMatcher (interface), NearestDriverMatcher
├── notification/   NotificationService (interface), ConsoleNotificationService
└── service/        RideService
```

| Package | Role |
|---|---|
| `model` | Domain data: locations, people, rides and ride statuses |
| `pricing` | Fare calculation strategies |
| `repository` | Storage and retrieval of drivers |
| `matching` | Selection of the driver for a ride |
| `notification` | Sending messages to users |
| `service` | Coordination of the whole ride-request flow |

`RideService` is the central high-level class. It depends only on interfaces, and the concrete implementations are chosen and connected in `Main`.

---

## 4. Implementation Details

**Model.** `Location` stores a name and coordinates and can compute the distance to another location. `Driver` has a name, a car model, a location and an availability flag. `Passenger` stores an id and a name. `Ride` links a passenger, a driver, a pickup, a destination, a fare and a `RideStatus` (`REQUESTED`, `ACCEPTED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`).

**Pricing.** `FareCalculator` declares one method, `calculate(Location from, Location to)`. The three implementations use a base fare plus a price per kilometer, with different values for the standard, night and premium tariffs.

**Repository.** `DriverRepository` declares `save` and `findAvailable`. `InMemoryDriverRepository` keeps the drivers in a list and returns only those that are available.

**Matching.** `DriverMatcher` declares `findDriver`, which returns an `Optional<Driver>`. `NearestDriverMatcher` returns the available driver with the smallest distance to the pickup point.

**Notification.** `NotificationService` declares `notify`. `ConsoleNotificationService` prints the messages to the console.

**Ride service.** `RideService.requestRide` finds a driver, marks the driver unavailable, calculates the fare, creates the ride and sends notifications. `RideService.completeRide` sets the status to `COMPLETED`, frees the driver and notifies the passenger.

---

## 5. SOLID Principles

### 5.1 Single Responsibility Principle

Every class has exactly one reason to change.

| Class | Responsibility |
|---|---|
| `RideService` | Coordinates the ride flow only |
| `FareCalculator` implementations | Calculate the price |
| `NearestDriverMatcher` | Choose a driver |
| `InMemoryDriverRepository` | Store and retrieve drivers |
| `ConsoleNotificationService` | Send messages |

`RideService` does not calculate prices, search for drivers or print messages itself. It delegates each task to the appropriate class. If the matching rule changes, only the matcher is modified.

### 5.2 Open/Closed Principle

The code is open for extension but closed for modification. Pricing is defined by the `FareCalculator` interface. To add a new tariff, such as weekend or airport pricing, a new class implementing `FareCalculator` is created. `RideService` and the existing calculators remain unchanged.

### 5.3 Dependency Inversion Principle

High-level modules depend on abstractions, not on concrete classes. `RideService` receives four interfaces through its constructor: `DriverRepository`, `DriverMatcher`, `FareCalculator` and `NotificationService`. The concrete classes are selected in `Main`. Because of this, the in-memory repository can later be replaced with file or database storage, and console messages with SMS or push notifications, without changing `RideService`.

---

## 6. Testing and Validation

The application was tested manually by running `Main` with several scenarios:

| Scenario | Expected result |
|---|---|
| Passenger requests a ride, drivers are available | The nearest driver is assigned, the fare is calculated, and both sides are notified |
| Ride is completed | Status becomes `COMPLETED` and the driver is available again |
| Same trip with a different `FareCalculator` | Only the fare changes, with no other code modified |
| All drivers are busy | The passenger is notified that no drivers are available, and no ride is created |

**Running the project.** Requirements: JDK 11 or newer.

```bash
javac -d out $(find src -name "*.java")
java -cp out taxi.Main
```

**Program output:**

```
(paste the console output of Main here)
```

---

## 7. Limitations and Future Development

**Limitations:**

* Drivers are stored only in memory, so all data is lost when the program ends.
* Distance is computed on a flat plane and is not a real road distance.
* There is no payment, authentication, rating system or real-time tracking.
* Input validation is minimal (for example, coordinates and passenger data are not validated).
* The ride status is only partly used.

**Future development:**

* Creational design patterns in the next laboratory work, for example to create fare calculators and rides in a centralized way instead of constructing them manually in Main.
* Further patterns in later laboratories.
* File or database storage for drivers and rides.
* More vehicle classes and tariffs.
* Input validation and unit tests.

---

## 8. Conclusion

In this laboratory work, I developed a simple online taxi application in Java and applied three SOLID principles: Single Responsibility, Open/Closed and Dependency Inversion.

The application separates pricing, driver matching, driver storage, notifications and ride coordination into focused components. This structure makes the code easier to understand, maintain, test and extend.

New tariffs can be introduced without changing the ride logic, while the in-memory repository and the console notifications can later be replaced with other implementations. The project provides a clear foundation for additional functionality and design patterns throughout the semester.