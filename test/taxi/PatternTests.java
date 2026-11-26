package taxi;

import taxi.domain.factory.EconomyDriverFactory;
import taxi.domain.models.*;
import taxi.legacy.LegacySmsGateway;
import taxi.matching.NearestDriverMatcher;
import taxi.notification.NotificationService;
import taxi.patterns.adapter.SmsNotificationAdapter;
import taxi.patterns.decorator.BookingFeeDecorator;
import taxi.patterns.decorator.SurgePricingDecorator;
import taxi.patterns.proxy.AuthorizedRideBookingProxy;
import taxi.pricing.FareCalculator;
import taxi.pricing.StandardFareCalculator;
import taxi.repository.InMemoryDriverRepository;
import taxi.service.RideBooking;
import taxi.service.RideService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PatternTests {

    private static int passed = 0;
    private static int failed = 0;

    private static final Location A = new Location("A", 0, 0);
    private static final Location B = new Location("B", 3, 4);   // distance 5 from A

    public static void main(String[] args) {
        // Decorator
        run("decorator_stacked_gives_expected_result", PatternTests::decoratorStacked);
        run("decorator_order_changes_result", PatternTests::decoratorOrder);
        run("decorator_rejects_invalid_multiplier", PatternTests::decoratorInvalid);
        // Adapter
        run("adapter_translates_call_to_legacy_gateway", PatternTests::adapterTranslates);
        run("adapter_translates_error_code_to_exception", PatternTests::adapterError);
        run("adapter_uses_fallback_for_unknown_recipient", PatternTests::adapterFallback);
        // Proxy
        run("proxy_blocks_unauthorized_passenger", PatternTests::proxyBlocks);
        run("proxy_passes_authorized_call_unchanged", PatternTests::proxyPasses);
        run("proxy_always_allows_completing_a_ride", PatternTests::proxyComplete);
        // Interaction
        run("interaction_proxy_decorator_adapter_work_together", PatternTests::interaction);

        System.out.println("\n" + passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---------- Decorator ----------
    static void decoratorStacked() {
        FareCalculator base = (from, to) -> 100.0;
        FareCalculator stacked = new BookingFeeDecorator(new SurgePricingDecorator(base, 1.5), 10.0);
        assertEquals(160.0, stacked.calculate(A, B));   // 100 * 1.5 + 10
    }

    static void decoratorOrder() {
        FareCalculator base = (from, to) -> 100.0;
        FareCalculator feeAfterSurge = new BookingFeeDecorator(new SurgePricingDecorator(base, 1.5), 10.0);
        FareCalculator feeBeforeSurge = new SurgePricingDecorator(new BookingFeeDecorator(base, 10.0), 1.5);
        assertEquals(160.0, feeAfterSurge.calculate(A, B));
        assertEquals(165.0, feeBeforeSurge.calculate(A, B));   // (100 + 10) * 1.5
    }

    static void decoratorInvalid() {
        FareCalculator base = (from, to) -> 100.0;
        assertThrows(IllegalArgumentException.class, () -> new SurgePricingDecorator(base, 0.5));
    }

    // ---------- Adapter ----------
    static void adapterTranslates() {
        RecordingGateway gateway = new RecordingGateway();
        Map<String, String> phones = new HashMap<>();
        phones.put("Alex", "+37360000001");
        NotificationService adapter = new SmsNotificationAdapter(gateway, phones);

        adapter.notify("Alex", "hello");

        assertEquals(1, gateway.calls);
        assertEquals("+37360000001", gateway.phone);
        assertEquals("hello", gateway.text);
        assertEquals(1, gateway.priority);
    }

    static void adapterError() {
        RecordingGateway gateway = new RecordingGateway();
        gateway.returnCode = 5;
        NotificationService adapter = new SmsNotificationAdapter(gateway, new HashMap<>());
        assertThrows(IllegalStateException.class, () -> adapter.notify("Alex", "hi"));
    }

    static void adapterFallback() {
        RecordingGateway gateway = new RecordingGateway();
        NotificationService adapter = new SmsNotificationAdapter(gateway, new HashMap<>());
        adapter.notify("Nobody", "hi");
        assertEquals("unknown", gateway.phone);
    }

    // ---------- Proxy ----------
    static void proxyBlocks() {
        StubBooking real = new StubBooking();
        RideBooking proxy = new AuthorizedRideBookingProxy(real, Set.of(99));

        Optional<Ride> result = proxy.requestRide(new Passenger(99, "Mallory"), A, B);

        assertTrue(result.isEmpty(), "blocked passenger must get no ride");
        assertEquals(0, real.requestCalls);   // the real service was never reached
    }

    static void proxyPasses() {
        StubBooking real = new StubBooking();
        Passenger alex = new Passenger(1, "Alex");
        real.toReturn = sampleRide(alex);
        RideBooking proxy = new AuthorizedRideBookingProxy(real, Set.of(99));

        Optional<Ride> result = proxy.requestRide(alex, A, B);

        assertEquals(1, real.requestCalls);
        assertTrue(result.isPresent() && result.get() == real.toReturn, "result must pass through unchanged");
    }

    static void proxyComplete() {
        StubBooking real = new StubBooking();
        Passenger mallory = new Passenger(99, "Mallory");
        RideBooking proxy = new AuthorizedRideBookingProxy(real, Set.of(99));

        proxy.completeRide(sampleRide(mallory));

        assertEquals(1, real.completeCalls);
    }

    // ---------- Interaction: Proxy -> RideService -> Decorator + Adapter ----------
    static void interaction() {
        InMemoryDriverRepository repo = InMemoryDriverRepository.getInstance();
        repo.save(new EconomyDriverFactory().createDriver(1, "Ion", "Dacia", new Location("Near", 0, 1)));

        FareCalculator fare = new BookingFeeDecorator(
                new SurgePricingDecorator(new StandardFareCalculator(), 1.5), 10.0);
        RecordingGateway gateway = new RecordingGateway();
        NotificationService sms = new SmsNotificationAdapter(gateway, new HashMap<>());
        RideBooking booking = new AuthorizedRideBookingProxy(
                new RideService(repo, new NearestDriverMatcher(), fare, sms), Set.of(99));

        Optional<Ride> ride = booking.requestRide(new Passenger(1, "Alex"), A, B);

        assertTrue(ride.isPresent(), "ride should be created");
        assertEquals(85.0, ride.get().getFare());   // (20 + 6*5) * 1.5 + 10
        assertEquals(2, gateway.calls);             // passenger SMS + driver SMS
    }

    // ---------- helpers ----------
    static Ride sampleRide(Passenger p) {
        Driver d = new EconomyDriverFactory().createDriver(7, "Test", "Car", A);
        return Ride.builder().id(1).passenger(p).driver(d).pickup(A).destination(B).fare(10).build();
    }

    static class RecordingGateway extends LegacySmsGateway {
        int calls;
        String phone;
        String text;
        int priority;
        int returnCode = 0;

        @Override
        public int sendSms(String phoneNumber, String text, int priority) {
            calls++;
            this.phone = phoneNumber;
            this.text = text;
            this.priority = priority;
            return returnCode;
        }
    }

    static class StubBooking implements RideBooking {
        int requestCalls;
        int completeCalls;
        Ride toReturn;

        @Override
        public Optional<Ride> requestRide(Passenger p, Location pickup, Location destination) {
            requestCalls++;
            return Optional.ofNullable(toReturn);
        }

        @Override
        public Optional<Ride> requestRide(Passenger p, Location pickup, Location destination,
                                          PaymentMethod pm, boolean childSeat) {
            requestCalls++;
            return Optional.ofNullable(toReturn);
        }

        @Override
        public void completeRide(Ride ride) {
            completeCalls++;
        }
    }

    interface Check { void run() throws Exception; }

    static void run(String name, Check check) {
        try {
            check.run();
            passed++;
            System.out.println("PASS  " + name);
        } catch (Throwable e) {
            failed++;
            System.out.println("FAIL  " + name + " -> " + e.getMessage());
        }
    }

    static void assertEquals(double expected, double actual) {
        if (Math.abs(expected - actual) > 1e-9) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }

    static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }

    static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    static void assertThrows(Class<? extends Throwable> type, Check check) {
        try {
            check.run();
        } catch (Throwable e) {
            if (type.isInstance(e)) {
                return;
            }
            throw new AssertionError("expected " + type.getSimpleName() + " but got " + e);
        }
        throw new AssertionError("expected " + type.getSimpleName() + " but nothing was thrown");
    }
}