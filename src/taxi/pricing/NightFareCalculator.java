package taxi.pricing;

import taxi.domain.models.Location;

public class NightFareCalculator implements FareCalculator {
    private static final double BASE_FARE = 30.0;
    private static final double PRICE_PER_KM = 9.0;

    @Override
    public double calculate(Location from, Location to) {
        return BASE_FARE + from.distanceTo(to) * PRICE_PER_KM;
    }
}