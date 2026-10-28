package taxi.pricing;

import taxi.domain.models.Location;

public interface FareCalculator {
    double calculate(Location from, Location to);
}