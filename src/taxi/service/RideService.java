package taxi.service;

import taxi.matching.DriverMatcher;
import taxi.model.*;
import taxi.notification.NotificationService;
import taxi.pricing.FareCalculator;
import taxi.repository.DriverRepository;

import java.util.Optional;

public class RideService {
    private final DriverRepository driverRepository;
    private final DriverMatcher driverMatcher;
    private final FareCalculator fareCalculator;
    private final NotificationService notificationService;
    private int nextRideId = 1;

    // Dependencies are injected as abstractions (DIP)
    public RideService(DriverRepository driverRepository,
                       DriverMatcher driverMatcher,
                       FareCalculator fareCalculator,
                       NotificationService notificationService) {
        this.driverRepository = driverRepository;
        this.driverMatcher = driverMatcher;
        this.fareCalculator = fareCalculator;
        this.notificationService = notificationService;
    }

    public Optional<Ride> requestRide(Passenger passenger, Location pickup, Location destination) {
        Optional<Driver> match = driverMatcher.findDriver(pickup, driverRepository.findAvailable());

        if (match.isEmpty()) {
            notificationService.notify(passenger.getName(), "No drivers available right now.");
            return Optional.empty();
        }

        Driver driver = match.get();
        driver.setAvailable(false);

        double fare = fareCalculator.calculate(pickup, destination);
        Ride ride = new Ride(nextRideId++, passenger, driver, pickup, destination, fare);

        notificationService.notify(passenger.getName(),
                "Driver " + driver.getName() + " (" + driver.getCarModel() + ") is on the way. Fare: "
                        + String.format("%.2f", fare) + " MDL");
        notificationService.notify(driver.getName(), "New ride from " + pickup + " to " + destination);
        return Optional.of(ride);
    }

    public void completeRide(Ride ride) {
        ride.setStatus(RideStatus.COMPLETED);
        ride.getDriver().setAvailable(true);
        notificationService.notify(ride.getPassenger().getName(), "Ride completed. Thank you!");
    }
}