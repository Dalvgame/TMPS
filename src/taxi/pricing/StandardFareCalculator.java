package taxi.pricing;

import taxi.model.Location;

public class StandardFareCalculator implements FareCalculator {
    private static final double BASE_FARE = 20.0;
    private static final double PRICE_PER_KM = 6.0;

    @Override
    public double calculate(Location from, Location to) {
        return BASE_FARE + from.distanceTo(to) * PRICE_PER_KM;
    }
}