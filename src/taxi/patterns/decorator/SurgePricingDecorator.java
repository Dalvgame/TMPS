package taxi.patterns.decorator;

import taxi.domain.models.Location;
import taxi.pricing.FareCalculator;

public class SurgePricingDecorator extends FareCalculatorDecorator {
    private final double multiplier;

    public SurgePricingDecorator(FareCalculator wrapped, double multiplier) {
        super(wrapped);
        if (multiplier < 1.0) {
            throw new IllegalArgumentException("Surge multiplier must be >= 1.0");
        }
        this.multiplier = multiplier;
    }

    @Override
    public double calculate(Location from, Location to) {
        return wrapped.calculate(from, to) * multiplier;
    }
}