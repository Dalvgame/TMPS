package taxi.client;

import taxi.domain.factory.DriverFactory;
import taxi.domain.factory.EconomyDriverFactory;
import taxi.domain.factory.MinivanDriverFactory;
import taxi.domain.factory.PremiumDriverFactory;
import taxi.domain.models.*;
import taxi.matching.NearestDriverMatcher;
import taxi.notification.ConsoleNotificationService;
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

        // --- Singleton ---
        DriverRepository repo = InMemoryDriverRepository.getInstance();
        System.out.println("Same repository instance? "
                + (repo == InMemoryDriverRepository.getInstance()));

        // --- Factory Method ---
        DriverFactory economy = new EconomyDriverFactory();
        DriverFactory premium = new PremiumDriverFactory();
        DriverFactory minivan = new MinivanDriverFactory();

        repo.save(economy.createDriver(1, "Ion", "Dacia Logan", new Location("Ciocana", 2, 3)));
        repo.save(premium.createDriver(2, "Maria", "Mercedes E-Class", new Location("Rascani", -1, 1)));
        repo.save(minivan.createDriver(3, "Petru", "Dacia Lodgy", new Location("Buiucani", 5, 5)));

        Passenger alex = new Passenger(1, "Alex");

        // Standard tariff, default options (cash, no child seat)
        RideService standardService = new RideService(
                repo, new NearestDriverMatcher(),
                new StandardFareCalculator(), new ConsoleNotificationService());

        Optional<Ride> ride = standardService.requestRide(alex, center, airport);
        ride.ifPresent(r -> {
            System.out.println(r);
            standardService.completeRide(r);
        });

        System.out.println("---- night tariff, card payment, child seat (Builder options) ----");
        RideService nightService = new RideService(
                repo, new NearestDriverMatcher(),
                new NightFareCalculator(), new ConsoleNotificationService());

        nightService.requestRide(alex, center, botanica, PaymentMethod.CARD, true)
                .ifPresent(System.out::println);
    }
}