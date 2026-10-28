package taxi.pricing;

import taxi.model.Location;

public interface FareCalculator {
    double calculate(Location from, Location to);
}