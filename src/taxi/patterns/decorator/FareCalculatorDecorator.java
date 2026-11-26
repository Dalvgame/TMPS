package taxi.patterns.decorator;

import taxi.domain.models.Location;
import taxi.pricing.FareCalculator;

public abstract class FareCalculatorDecorator implements FareCalculator {
    protected final FareCalculator wrapped;

    protected FareCalculatorDecorator(FareCalculator wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public double calculate(Location from, Location to) {
        return wrapped.calculate(from, to);
    }
}