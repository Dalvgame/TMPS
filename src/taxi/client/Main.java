package taxi.client;

import taxi.domain.factory.DriverFactory;
import taxi.domain.factory.EconomyDriverFactory;
import taxi.domain.factory.PremiumDriverFactory;
import taxi.domain.models.*;
import taxi.legacy.LegacySmsGateway;
import taxi.matching.NearestDriverMatcher;
import taxi.notification.NotificationService;
import taxi.patterns.adapter.SmsNotificationAdapter;
import taxi.patterns.decorator.BookingFeeDecorator;
import taxi.patterns.decorator.DiscountDecorator;
import taxi.patterns.decorator.SurgePricingDecorator;
import taxi.patterns.proxy.AuthorizedRideBookingProxy;
import taxi.pricing.FareCalculator;
import taxi.pricing.StandardFareCalculator;
import taxi.repository.DriverRepository;
import taxi.repository.InMemoryDriverRepository;
import taxi.service.RideBooking;
import taxi.service.RideService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Location center = new Location("Centru", 0, 0);
        Location airport = new Location("Aeroport", 10, 4);

        // --- Lab 2 creational patterns still in use ---
        DriverRepository repo = InMemoryDriverRepository.getInstance();      // Singleton
        DriverFactory economy = new EconomyDriverFactory();                  // Factory Method
        DriverFactory premium = new PremiumDriverFactory();
        repo.save(economy.createDriver(1, "Ion", "Dacia Logan", new Location("Ciocana", 2, 3)));
        repo.save(premium.createDriver(2, "Maria", "Mercedes E-Class", new Location("Rascani", -1, 1)));

        // --- Decorator: stack pricing rules at runtime ---
        FareCalculator fare = new BookingFeeDecorator(
                new SurgePricingDecorator(new StandardFareCalculator(), 1.5), 10.0);

        // --- Adapter: legacy SMS gateway behind our NotificationService interface ---
        Map<String, String> phoneBook = new HashMap<>();
        phoneBook.put("Alex", "+37360000001");
        phoneBook.put("Ion", "+37360000002");
        phoneBook.put("Maria", "+37360000003");
        NotificationService sms = new SmsNotificationAdapter(new LegacySmsGateway(), phoneBook);

        // --- Proxy: access control in front of the real service ---
        RideBooking booking = new AuthorizedRideBookingProxy(
                new RideService(repo, new NearestDriverMatcher(), fare, sms),
                Set.of(99));                                                 // passenger 99 is blocked

        Passenger alex = new Passenger(1, "Alex");
        Passenger mallory = new Passenger(99, "Mallory");

        System.out.println("=== 1. Authorized passenger: Proxy -> RideService -> Decorators + Adapter ===");
        booking.requestRide(alex, center, airport).ifPresent(r -> {
            System.out.println(r);
            booking.completeRide(r);
        });

        System.out.println("\n=== 2. Blocked passenger: stopped by the Proxy ===");
        booking.requestRide(mallory, center, airport);

        System.out.println("\n=== 3. New decorator added at runtime, no existing class edited ===");
        FareCalculator promoFare = new DiscountDecorator(fare, 20);
        RideBooking promoBooking = new AuthorizedRideBookingProxy(
                new RideService(repo, new NearestDriverMatcher(), promoFare, sms),
                Set.of(99));
        promoBooking.requestRide(alex, center, airport).ifPresent(System.out::println);
    }
}