package taxi;

import taxi.matching.NearestDriverMatcher;
import taxi.model.*;
import taxi.notification.ConsoleNotificationService;
import taxi.pricing.FareCalculator;
import taxi.pricing.NightFareCalculator;
import taxi.pricing.StandardFareCalculator;
import taxi.repository.DriverRepository;
import taxi.repository.InMemoryDriverRepository;
import taxi.service.RideService;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Location center = new Location("Centru", 0, 0);
        Location airport = new Location("Aeroport", 10, 4);
        Location botanica = new Location("Botanica", 3, -2);

        DriverRepository repo = new InMemoryDriverRepository();
        repo.save(new Driver(1, "Ion", "Dacia Logan", new Location("Ciocana", 2, 3)));
        repo.save(new Driver(2, "Maria", "Toyota Prius", new Location("Rascani", -1, 1)));

        // Swap this for NightFareCalculator and nothing else changes (OCP)
        FareCalculator calculator = new StandardFareCalculator();

        RideService service = new RideService(
                repo,
                new NearestDriverMatcher(),
                calculator,
                new ConsoleNotificationService());

        Passenger alex = new Passenger(1, "Alex");

        Optional<Ride> ride = service.requestRide(alex, center, airport);
        ride.ifPresent(r -> {
            System.out.println(r);
            service.completeRide(r);
        });

        System.out.println("---- same trip with night tariff ----");
        RideService nightService = new RideService(
                repo, new NearestDriverMatcher(),
                new NightFareCalculator(), new ConsoleNotificationService());
        nightService.requestRide(alex, center, botanica).ifPresent(System.out::println);
    }
}