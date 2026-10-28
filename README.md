# Technical University of Moldova
**Faculty of Computers, Informatics and Microelectronics**

## Laboratory Work No. 2

**Subject:** Tehnici și Mecanisme de Proiectare Software (TMPS)

**Topic:** Creational Design Patterns

**Project:** Online Taxi Application

**Mentor:** asist. univ. Istrati Daniel

**Student:** Cuibari Vladislav

**Group:** FAF-243

---

## Table of Contents

1. Introduction and Objectives
2. Domain Description
3. Project Architecture
4. Implementation Details
5. Creational Design Patterns
6. Testing and Validation
7. Limitations and Future Development
8. Conclusion

---

## 1. Introduction and Objectives

This laboratory work extends the online taxi application from Laboratory Work No. 1. Creational design patterns deal with object creation: they try to create objects in a manner suitable to the situation, hiding or controlling the instantiation logic so that the rest of the code stays simple.

In the first version of the project, objects were created manually with `new` in Main and inside services. This works for a small example, but it causes problems as the project grows: nothing prevents several repositories from being created, a class with many optional fields needs a long and confusing constructor, and client code is tied to concrete classes.

**Objectives:**

1. Study and understand the creational design patterns.
2. Choose a domain, define its main classes and choose the appropriate instantiation mechanisms.
3. Implement at least three creational design patterns in a sample project.
4. Organize the project into the required packages: `client`, `domain/models` and `domain/factory`.
5. Keep the SOLID principles from Laboratory Work No. 1 intact.

The project is written in Java, without any frameworks or libraries.

**Patterns implemented:**

* Singleton;
* Builder;
* Factory Method.

---

## 2. Domain Description

The domain is a ride-hailing (taxi) service. The main entities are:

| Entity | Description |
|---|---|
| `Location` | A named point with coordinates; can compute the distance to another location |
| `Passenger` | A person who requests a ride |
| `Driver` | A person who drives a vehicle and can be available or busy |
| `Vehicle` | The car used by a driver; can be an `EconomyCar`, `PremiumCar` or `MinivanCar` |
| `Ride` | A trip linking a passenger, a driver, a pickup, a destination, a fare and optional settings |
| `PaymentMethod` | `CASH` or `CARD` |
| `RideStatus` | `REQUESTED`, `ACCEPTED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED` |

**Instantiation needs identified:**

| Need | Mechanism chosen |
|---|---|
| The driver storage must exist exactly once and be shared | Singleton |
| A `Ride` has required and optional data and must be validated | Builder |
| Drivers must receive different vehicle types without the client choosing concrete classes | Factory Method |

---

## 3. Project Architecture

```
src/taxi/
├── client/
│   └── Main.java
├── domain/
│   ├── models/
│   │   ├── Location.java
│   │   ├── Passenger.java
│   │   ├── Driver.java
│   │   ├── Ride.java
│   │   ├── RideStatus.java
│   │   ├── PaymentMethod.java
│   │   ├── Vehicle.java
│   │   ├── EconomyCar.java
│   │   ├── PremiumCar.java
│   │   └── MinivanCar.java
│   └── factory/
│       ├── DriverFactory.java
│       ├── EconomyDriverFactory.java
│       ├── PremiumDriverFactory.java
│       └── MinivanDriverFactory.java
├── pricing/
│   ├── FareCalculator.java
│   ├── StandardFareCalculator.java
│   ├── NightFareCalculator.java
│   └── PremiumFareCalculator.java
├── repository/
│   ├── DriverRepository.java
│   └── InMemoryDriverRepository.java
├── matching/
│   ├── DriverMatcher.java
│   └── NearestDriverMatcher.java
├── notification/
│   ├── NotificationService.java
│   └── ConsoleNotificationService.java
└── service/
    └── RideService.java
```

| Package | Role |
|---|---|
| `client` | Entry point of the application |
| `domain.models` | Domain entities and enums |
| `domain.factory` | Factories that create drivers and their vehicles |
| `pricing` | Fare calculation strategies |
| `repository` | Driver storage (Singleton) |
| `matching` | Driver selection |
| `notification` | Messages to users |
| `service` | Coordination of the ride-request flow |

---

## 4. Implementation Details

**Models.** `Vehicle` is an abstract class that stores the car model and declares `getCategory()` and `getSeats()`. `EconomyCar`, `PremiumCar` and `MinivanCar` extend it with their own category and seat count. `Driver` holds a `Vehicle`. `Ride` holds all the data of a trip, including a payment method and a child-seat flag.

**Factories.** `DriverFactory` is an abstract class with an abstract method `createVehicle(String model)` and a concrete method `createDriver(...)` that builds a `Driver` using the vehicle returned by the subclass.

**Repository.** `InMemoryDriverRepository` implements `DriverRepository` and is available only through `getInstance()`.

**Ride service.** `RideService.requestRide` finds the nearest driver, marks the driver unavailable, calculates the fare, creates the ride through `Ride.builder()` and sends notifications. An overloaded version accepts the payment method and the child-seat option.

---

## 5. Creational Design Patterns

### 5.1 Singleton: `InMemoryDriverRepository`

**Problem.** Every `RideService` must see the same drivers and the same availability. If different parts of the program created their own repositories, the data would be inconsistent.

**Solution.** The constructor is private, a static field holds the single instance, and the static method `getInstance()` creates it on first use and returns the same object afterwards. The method is `synchronized` to be safe in a multithreaded environment.

```java
public class InMemoryDriverRepository implements DriverRepository {
    private static InMemoryDriverRepository instance;
    private final List<Driver> drivers = new ArrayList<>();

    private InMemoryDriverRepository() { }

    public static synchronized InMemoryDriverRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryDriverRepository();
        }
        return instance;
    }
    // save(...), findAvailable() ...
}
```

`Main` verifies the pattern by comparing two calls to `getInstance()` with `==`. The instance is stored in a variable of type `DriverRepository`, so `RideService` still depends on the abstraction (Dependency Inversion from Lab 1).

### 5.2 Builder: `Ride.Builder`

**Problem.** A `Ride` has required data (id, passenger, driver, pickup, destination, fare) and optional data (payment method, child seat). A constructor with many parameters is hard to read and easy to misuse, because the order of the arguments is easy to confuse.

**Solution.** The `Ride` constructor is private. The nested `Ride.Builder` class has one method per field, each returning the builder itself, so calls can be chained. Optional fields have defaults (`CASH`, no child seat). The `build()` method validates the required fields and the fare before creating the object.

```java
Ride ride = Ride.builder()
        .id(nextRideId++)
        .passenger(passenger)
        .driver(driver)
        .pickup(pickup)
        .destination(destination)
        .fare(fare)
        .paymentMethod(PaymentMethod.CARD)
        .childSeat(true)
        .build();
```

A `Ride` can only be created through the builder, so an invalid ride (for example without a driver) can never exist.

### 5.3 Factory Method: `DriverFactory`

**Problem.** Drivers drive different kinds of vehicles. If client code created `new EconomyCar(...)`, `new PremiumCar(...)` and so on, it would be tied to every concrete class, and adding a new category would require changing it.

**Solution.** `DriverFactory` declares the factory method `createVehicle(String model)`. Each subclass overrides it to return its own kind of vehicle, while `createDriver(...)` stays the same in the base class.

```java
public abstract class DriverFactory {
    protected abstract Vehicle createVehicle(String model);

    public Driver createDriver(int id, String name, String carModel, Location location) {
        return new Driver(id, name, createVehicle(carModel), location);
    }
}

public class EconomyDriverFactory extends DriverFactory {
    @Override
    protected Vehicle createVehicle(String model) {
        return new EconomyCar(model);
    }
}
```

Client code works only with `DriverFactory` and `Vehicle`. A new category (for example `ElectricCar`) means a new `Vehicle` subclass and a new factory, with no changes to existing code, which also respects the Open/Closed Principle.

### 5.4 Summary

| Pattern | Class | Benefit |
|---|---|---|
| Singleton | `InMemoryDriverRepository` | One shared, consistent storage |
| Builder | `Ride.Builder` | Readable creation, defaults and validation |
| Factory Method | `DriverFactory` and subclasses | Client independent of concrete vehicle classes |

---

## 6. Testing and Validation

The application was tested manually by running `Main`.

| Scenario | Expected result |
|---|---|
| Two calls to `InMemoryDriverRepository.getInstance()` | Both return the same object (`true`) |
| Drivers created through the three factories | Each driver has the matching vehicle type and seat count |
| Ride requested with defaults | Payment is `CASH`, no child seat |
| Ride requested with card payment and child seat | The ride shows `CARD` and `child seat` |
| Builder called without a driver | `IllegalStateException` is thrown |
| Ride completed | Status becomes `COMPLETED` and the driver is available again |
| All drivers are busy | The passenger is notified and no ride is created |

**Running the project.** Requirements: JDK 11 or newer. From the repository root:

```bash
javac -d out $(find src -name "*.java")
java -cp out taxi.client.Main
```

**Program output:**

```
(paste the console output of Main here)
```

---

## 7. Limitations and Future Development

**Limitations:**

* Data is stored only in memory and is lost when the program ends.
* The Singleton uses a `synchronized` method, which is simple but not the most efficient option, and global state makes unit testing harder.
* Distance is computed on a flat plane, not as a real road distance.
* There is no payment processing, authentication or real-time tracking.
* The project has no automated tests.

**Future development:**

* Additional creational patterns such as Prototype (cloning a ride for a repeated trip), Object Pool (reusing expensive objects) or Abstract Factory (creating families of related objects).
* Structural and behavioral patterns in the next laboratory works.
* File or database storage.
* Unit tests.

---

## 8. Conclusion

In this laboratory work, I extended the online taxi application with three creational design patterns: Singleton, Builder and Factory Method. The Singleton guarantees one shared driver repository, the Builder makes the creation of rides readable and safe, and the Factory Method removes the dependency of client code on concrete vehicle classes.

The project was reorganized into the `client` and `domain` (`models`, `factory`) packages, and it still respects the SOLID principles from the previous laboratory work. The result is code that is easier to read, harder to misuse and easier to extend with new patterns during the rest of the semester.